package org.bower.registry.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.*;

import org.bower.registry.domain.Package;
import org.bower.registry.repositories.PackageRepository;
import org.bower.registry.PackageBuilder;
import org.bower.registry.RegistryApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RegistryApplication.class)
@WebAppConfiguration
public class ApiControllerTests {
  private final static String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
  
  @Autowired
  private ApiController apiController;
  
  @Autowired
  private PackageRepository packageRepository;

  private MockMvc mockMvc;
  
  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();
  }
  
	@Test
	public void findAll_PackagesFound() throws Exception {
    final long P1_ID = 1l;
    final String P1_NAME = "foo";
    final String P1_URL = "http://foo";
    final long P2_ID = 2l;
    final String P2_NAME = "bar";
    final String P2_URL = "http://bar";
	  
    Package p1 = new PackageBuilder()
      .id(P1_ID).name(P1_NAME).url(P1_URL)
      .build();
    Package p2 = new PackageBuilder()
      .id(P2_ID).name(P2_NAME).url(P2_URL)
      .build();
	  
    packageRepository.save(p1);
    packageRepository.save(p2);

	  this.mockMvc.perform(get("/packages"))
	    .andDo(print())
	    .andExpect(status().isOk())
	    .andExpect(content().contentType(CONTENT_TYPE))	    
	    .andExpect(jsonPath("$", hasSize(2)))
	    .andExpect(jsonPath("$[0].id", is(2)))
      .andExpect(jsonPath("$[0].name", is(P2_NAME)))
      .andExpect(jsonPath("$[0].url", is(P2_URL)))
      .andExpect(jsonPath("$[1].id", is(1)))
      .andExpect(jsonPath("$[1].name", is(P1_NAME)))
      .andExpect(jsonPath("$[1].url", is(P1_URL)));
	}
}
