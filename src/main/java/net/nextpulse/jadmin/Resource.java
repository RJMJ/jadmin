package net.nextpulse.jadmin;

import net.nextpulse.jadmin.dao.AbstractDAO;
import net.nextpulse.jadmin.dsl.*;
import net.nextpulse.jadmin.elements.PageElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Top level configuration object for a resourceSchemaProvider that can be managed through JAdmin.
 *
 * @author yholkamp
 */
public class Resource {
  /**
   * Internal 'table' name of this resource
   */
  private final String tableName;
  /**
   * Column names to display on the index/list page.
   */
  private final List<String> indexColumns = new ArrayList<>();
  /**
   * Page elements to  display on the form (create & edit) pages.
   */
  private final List<PageElement> formPage = new ArrayList<>();
  /**
   * List of the column definitions for this resourceSchemaProvider, defining the column type and other properties.
   */
  private List<ColumnDefinition> columnDefinitions = new ArrayList<>();
  /**
   * Data access object handling object retrieval and persistence for this Resource.
   */
  private AbstractDAO dao;
  private int perPageCount = 20;
  private ValidationFunction afterValidation;
  private ValidationFunction beforeValidation;
  
  public Resource(String tableName) {
    if(tableName == null) {
      throw new NullPointerException("tableName was null");
    }
    this.tableName = tableName;
  }
  
  public Set<String> getEditableColumns() {
    return columnDefinitions.stream().filter(ColumnDefinition::isEditable).map(ColumnDefinition::getName).collect(Collectors.toSet());
  }
  
  public String getTableName() {
    return tableName;
  }
  
  public List<String> getIndexColumns() {
    return indexColumns;
  }
  
  public List<PageElement> getFormPage() {
    return formPage;
  }
  
  public List<String> getPrimaryKeys() {
    return columnDefinitions.stream().filter(ColumnDefinition::isKeyColumn).map(ColumnDefinition::getName).collect(Collectors.toList());
  }
  
  public List<ColumnDefinition> getColumnDefinitions() {
    return columnDefinitions;
  }
  
  public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
    this.columnDefinitions = columnDefinitions;
  }
  
  public AbstractDAO getDao() {
    return dao;
  }
  
  public void setDao(AbstractDAO dao) {
    this.dao = dao;
  }
  
  /**
   * Marks the provided column as editable.
   *
   * @param name internal name of the column to mark as editable
   */
  public void addEditableColumn(String name) {
    findColumnDefinitionByName(name)
        .orElseThrow(() -> new IllegalArgumentException("Column " + name + " could not be found on resource " + tableName))
        .setEditable(true);
  }
  
  /**
   * Marks the provided column as editable, configuring a validation function to run on the user input.
   *
   * @param name                 name of this column
   * @param inputValidationRules validates the user input for this column
   */
  public void addEditableColumn(String name, InputValidationRule... inputValidationRules) {
    findColumnDefinitionByName(name)
        .orElseThrow(() -> new IllegalArgumentException("Column " + name + " could not be found on resource " + tableName))
        .setEditable(true)
        .addValidationRules(inputValidationRules);
  }
  
  /**
   * Marks the provided column as editable, configuring a validation function to run on the user input.
   *
   * @param name             name of this column
   * @param inputTransformer transforms the user input for this column
   */
  public void addEditableColumn(String name, InputTransformer inputTransformer) {
    findColumnDefinitionByName(name)
        .orElseThrow(() -> new IllegalArgumentException("Column " + name + " could not be found on resource " + tableName))
        .setEditable(true)
        .setInputTransformer(inputTransformer);
  }
  
  /**
   * Marks the provided column as editable, configuring a validation function to run on the user input.
   *
   * @param name                 name of this column
   * @param inputValidationRules validates the user input for this column
   * @param inputTransformer     transforms the user input for this column
   */
  public void addEditableColumn(String name, InputTransformer inputTransformer, InputValidationRule... inputValidationRules) {
    findColumnDefinitionByName(name)
        .orElseThrow(() -> new IllegalArgumentException("Column " + name + " could not be found on resource " + tableName))
        .setEditable(true)
        .addValidationRules(inputValidationRules)
        .setInputTransformer(inputTransformer);
  }

  /**
   * Adds the provided column to the table on the index page and adds a transformation function for displaying the value
   * @param columnId                name of this column
   */
  public void addColumn(String columnId) {
    getIndexColumns().add(columnId);
  }

  /**
   * Adds the provided column to the table on the index page and adds a transformation function for displaying the value
   *
   * @param columnId                 name of this column
   * @param columnValueTransformer   transforms the way the existing column value is displayed
   */
  public void addColumn(String columnId, ColumnValueTransformer columnValueTransformer) {
    getIndexColumns().add(columnId);
    findColumnDefinitionByName(columnId)
      .orElseThrow(() -> new IllegalArgumentException("Column " + columnId + " could not be found on resource " + tableName))
      .setColumnValueTransformer(columnValueTransformer);
  }
  
  /**
   * Locates the column definition identified by 'name'
   *
   * @param name internal name of the column to retrieve the column definition for
   * @return either the ColumnDefinition or an empty Optional
   */
  public Optional<ColumnDefinition> findColumnDefinitionByName(String name) {
    return columnDefinitions.stream().filter(x -> x.getName().equals(name)).findFirst();
  }
  
  /**
   * Sets the number of entries to show on the resource list page
   *
   * @param perPageCount number of entries
   */
  public void setPerPageCount(int perPageCount) {
    this.perPageCount = perPageCount;
  }
  
  public int getPerPageCount() {
    return perPageCount;
  }
  
  
  /**
   * Sets a validation function to execute before the per-column validation.
   *
   * @param beforeValidation function to execute before the per-column validation
   */
  public void setBeforeValidation(ValidationFunction beforeValidation) {
    this.beforeValidation = beforeValidation;
  }
  
  /**
   * @return the configured validation function to execute before column validation
   */
  public ValidationFunction getBeforeValidation() {
    return beforeValidation;
  }
  
  /**
   * Sets a validation function to execute after the per-column validation.
   *
   * @param afterValidation function to execute after the per-column validation
   */
  public void setAfterValidation(ValidationFunction afterValidation) {
    this.afterValidation = afterValidation;
  }
  
  /**
   * @return the configured validation function to execute after column validation
   */
  public ValidationFunction getAfterValidation() {
    return afterValidation;
  }
}
