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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RegistryApplication.class)
@WebAppConfiguration
public class ApiControllerTests extends AbstractTransactionalJUnit4SpringContextTests {
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
  public void getPackages_PackagesFound() throws Exception {
    final String P1_NAME = "foo";
    final String P1_URL  = "http://foo";
    final String P2_NAME = "bar";
    final String P2_URL  = "http://bar";
    
    Package p1 = new PackageBuilder()
      .name(P1_NAME).url(P1_URL)
      .build();
    Package p2 = new PackageBuilder()
      .name(P2_NAME).url(P2_URL)
      .build();
    
    Long p1Id = packageRepository.save(p1).getId();
    Long p2Id = packageRepository.save(p2).getId();

    this.mockMvc.perform(get("/packages"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(CONTENT_TYPE))     
      .andExpect(jsonPath("$",         hasSize(2)))
      .andExpect(jsonPath("$[0].id",   is(p2Id.intValue())))
      .andExpect(jsonPath("$[0].name", is(P2_NAME)))
      .andExpect(jsonPath("$[0].url",  is(P2_URL)))
      .andExpect(jsonPath("$[1].id",   is(p1Id.intValue())))
      .andExpect(jsonPath("$[1].name", is(P1_NAME)))
      .andExpect(jsonPath("$[1].url",  is(P1_URL)));
  }
  
  @Test
  public void getPackage_PackageFound() throws Exception {
    final String P1_NAME = "foo";
    final String P1_URL  = "http://foo";
    final String P2_NAME = "bar";
    final String P2_URL  = "http://bar";
    
    Package p1 = new PackageBuilder()
      .name(P1_NAME).url(P1_URL)
      .build();
    Package p2 = new PackageBuilder()
      .name(P2_NAME).url(P2_URL)
      .build();
    
    Long p1Id = packageRepository.save(p1).getId();
    Long p2Id = packageRepository.save(p2).getId();

    this.mockMvc.perform(get("/packages/bar"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(CONTENT_TYPE))
      .andExpect(jsonPath("$.id",   is(p2Id.intValue())))
      .andExpect(jsonPath("$.name", is(P2_NAME)))
      .andExpect(jsonPath("$.url",  is(P2_URL)));
  }
    
  @Test
  public void postPackage() throws Exception {
    String payload = "{\"name\":\"foo\",\"url\":\"http://foo\"}";
    
    this.mockMvc.perform(
      post("/packages")
        .contentType(CONTENT_TYPE)
        .content(payload))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(content().contentType(CONTENT_TYPE))     
      .andExpect(jsonPath("$.name", is("foo")))
      .andExpect(jsonPath("$.url",  is("http://foo")));
  }
  
  @Test
  public void deletePackage() throws Exception {
    final String P1_NAME = "foo";
    final String P1_URL  = "http://foo";
    final String P2_NAME = "bar";
    final String P2_URL  = "http://bar";
    
    Package p1 = new PackageBuilder()
      .name(P1_NAME).url(P1_URL)
      .build();
    Package p2 = new PackageBuilder()
      .name(P2_NAME).url(P2_URL)
      .build();
    
    Long p1Id = packageRepository.save(p1).getId();
    Long p2Id = packageRepository.save(p2).getId();

    this.mockMvc.perform(
      delete("/packages/" + P1_NAME)
        .contentType(CONTENT_TYPE))
      .andDo(print())
      .andExpect(status().isOk());

    this.mockMvc.perform(get("/packages"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(CONTENT_TYPE))     
      .andExpect(jsonPath("$",         hasSize(1)))
      .andExpect(jsonPath("$[0].id",   is(p2Id.intValue())))
      .andExpect(jsonPath("$[0].name", is(P2_NAME)))
      .andExpect(jsonPath("$[0].url",  is(P2_URL)));
  }
  
  @Test
  public void searchPackages_PackagesFound() throws Exception {
    final String P1_NAME = "foo";
    final String P1_URL  = "http://foo";
    final String P2_NAME = "bar";
    final String P2_URL  = "http://bar";
    final String P3_NAME = "foobar";
    final String P3_URL  = "http://foobar";
    final String P4_NAME = "barfoo";
    final String P4_URL  = "http://barfoo";
    
    Package p1 = new PackageBuilder()
      .name(P1_NAME).url(P1_URL)
      .build();
    Package p2 = new PackageBuilder()
      .name(P2_NAME).url(P2_URL)
      .build();
    Package p3 = new PackageBuilder()
      .name(P3_NAME).url(P3_URL)
      .build();
    Package p4 = new PackageBuilder()
      .name(P4_NAME).url(P4_URL)
      .build();
    
    Long p1Id = packageRepository.save(p1).getId();
    Long p2Id = packageRepository.save(p2).getId();
    Long p3Id = packageRepository.save(p3).getId();
    Long p4Id = packageRepository.save(p4).getId();

    this.mockMvc.perform(get("/packages/search/foo"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().contentType(CONTENT_TYPE))     
      .andExpect(jsonPath("$",         hasSize(3)))
      .andExpect(jsonPath("$[0].id",   is(p4Id.intValue())))
      .andExpect(jsonPath("$[0].name", is(P4_NAME)))
      .andExpect(jsonPath("$[0].url",  is(P4_URL)))
      .andExpect(jsonPath("$[1].id",   is(p1Id.intValue())))
      .andExpect(jsonPath("$[1].name", is(P1_NAME)))
      .andExpect(jsonPath("$[1].url",  is(P1_URL)))
      .andExpect(jsonPath("$[2].id",   is(p3Id.intValue())))
      .andExpect(jsonPath("$[2].name", is(P3_NAME)))
      .andExpect(jsonPath("$[2].url",  is(P3_URL)));
  }
}
