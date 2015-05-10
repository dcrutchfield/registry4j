package org.bower.registry;

import org.bower.registry.domain.Package;

public class PackageBuilder {
  private Package _package;
  
  public PackageBuilder() {
    this._package = new Package();
  }
  
  public PackageBuilder id(long id) {
    this._package.setId(id);
    
    return this;
  }
  
  public PackageBuilder name(String name) {
    this._package.setName(name);
    
    return this;
  }
  
  public PackageBuilder url(String url) {
    this._package.setUrl(url);
    
    return this;
  }
  
  public Package build() {
    return _package;
  }
}
