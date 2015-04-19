package org.bower.registry.repositories;

import java.util.List;

import org.bower.registry.domain.Package;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PackageRepository extends PagingAndSortingRepository<Package, Long>{
  List<Package> findByNameLike(String name, Sort order);
	
	Package findByName(String name);
	
	void deletePackageByName(String name);
}
