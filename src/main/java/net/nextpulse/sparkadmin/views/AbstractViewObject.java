package net.nextpulse.sparkadmin.views;

/**
 * @author yholkamp
 */
public class AbstractViewObject {

  protected TemplateObject templateObject;

  public TemplateObject getTemplateObject() {
    return templateObject;
  }

  public void setTemplateObject(TemplateObject templateObject) {
    this.templateObject = templateObject;
  }
}
