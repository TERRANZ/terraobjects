package ru.terraobject.entity.dao;

/**
 *
 * @author terranz
 */
public interface DAOConsts {
    public final String SELECT_ALL_OBJECTS = "select * from object";
    public final String SELECT_OBJECT_BY_ID = "select * from object where object_id = ?";
    public final String SELECT_OBJECTS_BY_TEMPLATE_ID = "select * from object where object_template_id = ?";
    public final String SELECT_OBJECTS_COUNT_BY_TEMPLATE_ID = "select COUNT(object_template_id) from object where object_template_id = ?";
    public final String SELECT_ALL_PROP_TYPES = "select * from prop_type";
    public final String SELECT_PROP_TYPE_BY_ID = "select * from prop_type where prop_type_id = ?";
    public final String SELECT_PROP_TYPE_BY_NAME = "select * from prop_type where prop_type_name like '%?%'";
    public final String SELECT_ALL_PROPERTIES = "select * from property";
    public final String SELECT_PROPERTY_BY_ID = "select * from property where prop_id = ?";
    public final String SELECT_ALL_OBJECT_TEMPLATE_PROPS = "select * from object_template_props";
    public final String SELECT_OBJECT_TEMPLATE_PROPS_BY_ID = "select * from object_template_props where "
	    + "object_template_props_id = ?";
    public final String SELECT_OBJECT_TEMPLATE_PROPS_BY_TEMPLATE_ID = "select * from object_template_props where "
	    + "object_template_id = ?";
    public final String SELECT_ALL_OBJECT_TEMPLATE = "select * from object_template";
    public final String SELECT_OBJECT_TEMPLATE_BY_ID = "select * from object_template where "
	    + "object_template_id = ?";
    public final String SELECT_OBJECT_PROPS_BY_OBJECT_ID = "select * from object_props where object_id = ?";
    public final String SELECT_OBJECT_PROP_BY_OBJECT_ID_AND_PROP_ID =
	    "select * from object_props where object_id = ? AND prop_id = ?";
    public final String SELECT_ALL_OBJECT_PROPS = "select * from object_props";
    public final String SELECT_OBJECT_PROPS_BY_PROP_ID = "select * from object_props where prop_id = ?";
}
