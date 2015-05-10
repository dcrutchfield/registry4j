package org.bower.registry.api;

import java.util.List;

import org.bower.registry.domain.Package;
import org.bower.registry.repositories.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	@Autowired
	private PackageRepository packageRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/status")
	public String getStatus() {
		return "";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/packages")
	public List<Package> getPackages() {
	  return (List<Package>) packageRepository.findAll(new Sort("name"));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/packages")
	@ResponseStatus(HttpStatus.CREATED)
	public Package postPackage(@RequestBody Package _package) {
		return packageRepository.save(_package);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/packages/{name}")
	public Package getPackage(@PathVariable String name) {
	  return packageRepository.findByName(name);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/packages/{name}")
	public void deletePackage(@PathVariable String name) {
	  packageRepository.deletePackageByName(name);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/packages/search/{name}")
	public List<Package> searchPackages(@PathVariable String name) {
	  return packageRepository.findByNameContaining(name, new Sort("name"));
	}
}