/**
 * License Agreement.
 *
 *  JBoss RichFaces 3.0 - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.highlight;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.uwyn.jhighlight.renderer.Renderer;

/**
 * @author Maksim Kaszynski
 *
 */
public class XhtmlRendererFactory {
	
	private static XhtmlRendererFactory instance;
	public static final String fileName = "renderers.properties";
	private Map<Object, Object> classNames = new HashMap<Object, Object>();

	public static final XhtmlRendererFactory instance() {
		synchronized(XhtmlRendererFactory.class) {
			if (instance == null) {
				instance = new XhtmlRendererFactory();
			}
		}
		
		return instance;
	}
	
	public XhtmlRendererFactory() {
		InputStream resourceAsStream = 
			getClass().getResourceAsStream(fileName);
		try {
			Properties props = new Properties();
			props.load(resourceAsStream);
			classNames.putAll(props);
			resourceAsStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public Renderer getRenderer(String type) {
		
		Renderer renderer = 
			com.uwyn.jhighlight.renderer.XhtmlRendererFactory.getRenderer(type);
		if (renderer == null) {
			Object object = classNames.get(type.toLowerCase());
			if (object != null) {
				String className = object.toString();
				
				try {
					Class<?> class1 = Class.forName(className);
					Object newInstance = class1.newInstance();
					if (newInstance instanceof Renderer) {
						return (Renderer) newInstance;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return renderer;
	}
	
	public static void main(String[] args) {
		Renderer renderer = 
			XhtmlRendererFactory.instance().getRenderer("css");
		String path = "D:\\eclipse\\rf\\rf3\\samples\\richfaces-demo\\src\\main\\webapp\\css\\common.css";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			renderer.highlight("css", new FileInputStream(path),bos, null, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		renderer = XhtmlRendererFactory.instance().getRenderer("xhtml");
		try {
			renderer.highlight("bbb", new ByteArrayInputStream(bos.toByteArray()), System.out, null, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
