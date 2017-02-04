package net.nextpulse.sparkadmin.dsl;

import net.nextpulse.sparkadmin.ColumnDefinition;
import net.nextpulse.sparkadmin.ColumnType;
import net.nextpulse.sparkadmin.exceptions.ConfigurationException;
import net.nextpulse.sparkadmin.Resource;
import net.nextpulse.sparkadmin.elements.FormInput;
import net.nextpulse.sparkadmin.elements.FormInputGroup;
import net.nextpulse.sparkadmin.elements.FormSelect;
import net.nextpulse.sparkadmin.helpers.Tuple2;

import java.util.List;
import java.util.function.Supplier;

/**
 * DSL class that provides a way to add a group of input fields to the resource.
 *
 * @author yholkamp
 */
public class InputGroupBuilder {

  private final FormInputGroup inputGroup;
  private final Resource resource;

  public InputGroupBuilder(FormInputGroup inputGroup, Resource resource) {
    this.inputGroup = inputGroup;
    this.resource = resource;
  }

  /**
   * Adds an input type field for the given column.
   *
   * @param column
   * @return
   */
  public InputGroupBuilder input(String column) {
    // ensure the column exists
    ColumnType columnType = getTypeForColumn(column);
    inputGroup.addInput(new FormInput(column, columnType));
    resource.addEditableColumn(column);
    return this;
  }

  /**
   * Retrieves the column type for the column of this table.
   * @param column
   * @return
   */
  private ColumnType getTypeForColumn(String column) {
    return resource.getColumnDefinitions().stream()
          .filter(x -> x.getName().equals(column))
          .map(ColumnDefinition::getType)
          .findFirst()
          .orElseThrow(() -> new ConfigurationException("Column " + column + " not found for table " + resource.getTableName()));
  }

  /**
   * Adds a select field to the form for the provided column, using the option values generated by the provided producer.
   *
   * @param column
   * @param selectProducer
   * @return
   */
  public InputGroupBuilder select(String column, Supplier<List<Tuple2<String, String>>> selectProducer) {
    ColumnType columnType = getTypeForColumn(column);
    inputGroup.addInput(new FormSelect(column, columnType, selectProducer));
    // consider validating that the output of the producer appears to be valid for the type of this column
    // consider passing the primary keys of the current object to the producer function
    return this;
  }
}
