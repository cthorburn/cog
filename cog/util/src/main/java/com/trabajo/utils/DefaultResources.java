package com.trabajo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DefaultResources implements IResources {
  
  private String path;
  private Properties aliases;
  
  public DefaultResources() {}

  /* (non-Javadoc)
   * @see org.worksync.common.IResources#getAliases()
   */
  public Properties getAliases() {
    return aliases;
  }

  public void setAliases(Properties aliases) {
    this.aliases = aliases;
  }

  public void setPath(String path) {
    this.path = path;
  }

  /* (non-Javadoc)
   * @see org.worksync.common.IResources#getPath()
   */
  public String getPath() {
    return path;
  }
  
  /* (non-Javadoc)
   * @see org.worksync.common.IResources#ResourcesAsStream(java.lang.String)
   */
  public InputStream asStream(String alias) {

    StringBuilder sb=new StringBuilder(128); 
    sb.append(path);
    sb.append('/');
    sb.append(aliases.getProperty(alias));
    
    return Thread.currentThread().getContextClassLoader().getResourceAsStream(sb.toString());
    
  }

  @Override
  public String asString(String alias) throws IOException {
      return IOUtils.stringFromUTF8Stream(asStream(alias));
  }
}
