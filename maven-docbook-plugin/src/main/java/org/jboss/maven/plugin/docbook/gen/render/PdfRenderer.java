package org.jboss.maven.plugin.docbook.gen.render;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

//import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.avalon.framework.logger.Logger;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.logging.Log;
import org.jboss.maven.plugin.docbook.gen.xslt.XSLTException;
import org.jboss.maven.plugin.docbook.gen.xslt.resolve.ResolverChain;

/**
 * Special handling for pdf rendering
 * 
 * @author Steve Ebersole
 */
public class PdfRenderer extends BasicRenderer {
    private FopFactory fopFactory;

    public PdfRenderer(RendererFactory factory, Formatting formatting) {
	super(factory, formatting);
    }

    /**
         * @param factory
         * @param formatting
         * @throws XSLTException
         */
    private FopFactory getFopFactory() throws RenderingException {
	if (null == fopFactory) {
	    fopFactory = FopFactory.newInstance();
	    final List resources = factory.getProject().getResources();
	    ResolverChain resolvers = new ResolverChain();
	    URIResolver resolver = new URIResolver() {

		public Source resolve(String href, String base)
			throws TransformerException {
		    getLog().info(
			    "Request to resource " + href
				    + " with document base " + base);
		    for (Iterator iter = resources.iterator(); iter.hasNext();) {
			Resource resource = (Resource) iter.next();
			String directory = resource.getDirectory();
			File res = new File(new File(directory), href);
			if (res.exists()) {
			    try {
				return new StreamSource(
					new FileInputStream(res));
			    } catch (FileNotFoundException e) {
				continue;
			    }
			}
		    }
		    return null;
		}

	    };
	    resolvers.addResolver(resolver);
	    try {
		resolvers.addResolver(factory.getTransformerFactory()
		    .buildUriResolver(formatting.getStandardDocBookSpec()));
	    } catch (XSLTException e) {
		    throw new RenderingException("Error create URIResolver for a fop transformation", e);
	    }
	    fopFactory.setURIResolver(resolvers);
	}
	return fopFactory;
    }

    protected Result buildResult(File targetFile) throws RenderingException {
	getLog().info(
		"building formatting result [" + targetFile.getAbsolutePath()
			+ "]");
	return new PdfResult(getFopFactory(),targetFile);
   }

    private static class PdfResult extends SAXResult {
	 private OutputStream out;

	public PdfResult(FopFactory fopFactory, File target) throws RenderingException {
	        super();
		try {
		    out = new BufferedOutputStream(
		    			    new java.io.FileOutputStream(target));
		    // Step 3: Construct fop with desired output format
		    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
		    // driver.setOutputStream( out );
		    setHandler(fop.getDefaultHandler());
		} catch (FileNotFoundException e) {
		    throw new RenderingException("unable to access target file "
			    + target.getAbsolutePath());
		} catch (FOPException e) {
		    throw new RenderingException("Error create FOP  renderer ", e);
		}
	}
	
	public void flush() throws IOException {
	    out.flush();
	    out.close();
	}
    }
    
    protected void releaseResult(Result transformationResult) {
        super.releaseResult(transformationResult);
        if (transformationResult instanceof PdfResult) {
	    PdfResult pdfResult = (PdfResult) transformationResult;
	    try {
		pdfResult.flush();
	    } catch (IOException e) {
		getLog().error("Error close output PDF file", e);
	    }
	}
    }
    

}
