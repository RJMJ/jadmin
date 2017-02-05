package net.nextpulse.jadmin.dsl;

import net.nextpulse.jadmin.Resource;

/**
 * DSL class that offers a way to configure which columns should be shown on the index page of a column.
 *
 * @author yholkamp
 */
public class IndexBuilder {
  private final Resource resource;

  public IndexBuilder(Resource resource) {
    this.resource = resource;
  }

  /**
   * Adds a column identified by id to the index page
   * @param id
   * @return
   */
  public IndexBuilder column(String id) {
    resource.getIndexColumns().add(id);
    return this;
  }

//  /**
//   * Adds edit/show/delete buttons to the index, defaults to the last column.
//   */
//  public IndexBuilder actions() {
//    return this;
//  }
}