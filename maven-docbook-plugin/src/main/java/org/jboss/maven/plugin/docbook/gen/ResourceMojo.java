package org.jboss.maven.plugin.docbook.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.resources.PropertyUtils;
import org.apache.maven.plugin.resources.ReflectionProperties;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.jboss.maven.plugin.docbook.gen.render.Formatting;
import org.jboss.maven.plugin.docbook.gen.render.RendererFactory;
import org.jboss.maven.plugin.docbook.gen.render.RenderingException;
import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;

/**
 * A DocBook plugin based on the excellent docbkx-maven-plugin, but which
 * specifically handles language translations in a more transparent way.
 *
 * @goal resources
 * @phase process-resources
 *
 * @author Steve Ebersole
 */
public class ResourceMojo extends AbstractDocBookMojo {

	private Properties filterProperties;


	protected void process(Formatting[] formattings) throws RenderingException, XSLTException {
		RendererFactory rendererFactory = new RendererFactory( options, null, targetDirectory, project, getLog() );
		try {
			for ( int i = 0; i < formattings.length; i++ ) {
				if ( formattings[i].isImageCopyingRequired() ) {
					File dir = rendererFactory.buildRenderer( formattings[i] ).prepareDirectory();
					copyResources( collectResource(), dir );
				}
			}
		}
		catch ( IOException e ) {
			throw new RenderingException( "unable to process resources", e );
		}
	}

	private List collectResource() {
		// todo : need a way to be able to get to the images defined by the xslt dependencies and include them
		//
		// for now, just return project resources
		return project.getResources();
	}

	protected void copyResources(List resources, File outputDir) throws IOException {
        initializeFiltering();

        for ( Iterator i = resources.iterator(); i.hasNext(); ) {
            Resource resource = ( Resource ) i.next();
            String targetPath = resource.getTargetPath();
            File resourceDirectory = new File( resource.getDirectory() );

            if ( !resourceDirectory.exists() ) {
                getLog().info( "Resource directory does not exist: " + resourceDirectory );
                continue;
            }

            if ( !outputDir.exists() ) {
                if ( !outputDir.mkdirs() ) {
                    throw new IOException( "Cannot create resource output directory: " + outputDir );
                }
            }

            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir( resource.getDirectory() );

			if ( resource.getIncludes() != null && !resource.getIncludes().isEmpty() ) {
                scanner.setIncludes( ( String[]) resource.getIncludes().toArray( new String[] {} ) );
            }
            else {
                scanner.setIncludes( new String[] { "**/**" } );
            }

            if ( resource.getExcludes() != null && !resource.getExcludes().isEmpty() ) {
                scanner.setExcludes( (String[]) resource.getExcludes().toArray( new String[] {} ) );
            }

            scanner.addDefaultExcludes();
            scanner.scan();

            List includedFiles = Arrays.asList( scanner.getIncludedFiles() );

            for ( Iterator j = includedFiles.iterator(); j.hasNext(); ) {
                String name = (String) j.next();
                String destination = name;

                if ( targetPath != null ) {
                    destination = targetPath + "/" + name;
                }

                File source = new File( resource.getDirectory(), name );
                File destinationFile = new File( outputDir, destination );

                if ( !destinationFile.getParentFile().exists() ) {
                    destinationFile.getParentFile().mkdirs();
                }

				copyFile( source, destinationFile, resource.isFiltering() );
            }
        }
    }

    private void initializeFiltering() throws IOException {
        filterProperties = new Properties();
        filterProperties.putAll( System.getProperties() );

        // Project properties
        filterProperties.putAll( project.getProperties() );

		Iterator itr = project.getBuild().getFilters().iterator();
		while ( itr.hasNext() ) {
            String filtersfile = ( String ) itr.next();
            Properties properties = PropertyUtils.loadPropertyFile( new File( filtersfile ), true, true );
            filterProperties.putAll( properties );
        }
    }

    private void copyFile( File from, File to, boolean filtering ) throws IOException {
        if ( !filtering )
        {
            if ( to.lastModified() < from.lastModified() )
            {
                FileUtils.copyFile( from, to );
            }
        }
        else
        {
            // buffer so it isn't reading a byte at a time!
            Reader fileReader = null;
            Writer fileWriter = null;
            try {
				fileReader = new BufferedReader( new FileReader( from ) );
				fileWriter = new FileWriter( to );

				// support ${token}
                Reader reader = new InterpolationFilterReader( fileReader, filterProperties, "${", "}" );

                // support @token@
                reader = new InterpolationFilterReader( reader, filterProperties, "@", "@" );

                boolean isPropertiesFile = false;

                if ( to.isFile() && to.getName().endsWith( ".properties" ) ) {
                    isPropertiesFile = true;
                }

                reader = new InterpolationFilterReader( reader, new ReflectionProperties( project, isPropertiesFile ), "${", "}" );

                IOUtil.copy( reader, fileWriter );
            }
            finally
            {
                IOUtil.close( fileReader );
                IOUtil.close( fileWriter );
            }
        }
    }
}
