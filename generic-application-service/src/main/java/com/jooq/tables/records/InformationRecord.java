/*
 * This file is generated by jOOQ.
*/
package com.jooq.tables.records;


import com.jooq.tables.Information;

import java.sql.Date;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InformationRecord extends UpdatableRecordImpl<InformationRecord> implements Record10<Integer, String, String, String, Date, String, String, String, String, String> {

    private static final long serialVersionUID = -1354641165;

    /**
     * Setter for <code>applications.information.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>applications.information.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>applications.information.company</code>.
     */
    public void setCompany(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>applications.information.company</code>.
     */
    public String getCompany() {
        return (String) get(1);
    }

    /**
     * Setter for <code>applications.information.position</code>.
     */
    public void setPosition(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>applications.information.position</code>.
     */
    public String getPosition() {
        return (String) get(2);
    }

    /**
     * Setter for <code>applications.information.location</code>.
     */
    public void setLocation(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>applications.information.location</code>.
     */
    public String getLocation() {
        return (String) get(3);
    }

    /**
     * Setter for <code>applications.information.dateApplied</code>.
     */
    public void setDateapplied(Date value) {
        set(4, value);
    }

    /**
     * Getter for <code>applications.information.dateApplied</code>.
     */
    public Date getDateapplied() {
        return (Date) get(4);
    }

    /**
     * Setter for <code>applications.information.contactName</code>.
     */
    public void setContactname(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>applications.information.contactName</code>.
     */
    public String getContactname() {
        return (String) get(5);
    }

    /**
     * Setter for <code>applications.information.contactMethod</code>.
     */
    public void setContactmethod(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>applications.information.contactMethod</code>.
     */
    public String getContactmethod() {
        return (String) get(6);
    }

    /**
     * Setter for <code>applications.information.contactedMeFirst</code>.
     */
    public void setContactedmefirst(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>applications.information.contactedMeFirst</code>.
     */
    public String getContactedmefirst() {
        return (String) get(7);
    }

    /**
     * Setter for <code>applications.information.status</code>.
     */
    public void setStatus(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>applications.information.status</code>.
     */
    public String getStatus() {
        return (String) get(8);
    }

    /**
     * Setter for <code>applications.information.notes</code>.
     */
    public void setNotes(String value) {
        set(9, value);
    }

    /**
     * Getter for <code>applications.information.notes</code>.
     */
    public String getNotes() {
        return (String) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, String, String, String, Date, String, String, String, String, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, String, String, String, Date, String, String, String, String, String> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Information.INFORMATION.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Information.INFORMATION.COMPANY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Information.INFORMATION.POSITION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Information.INFORMATION.LOCATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Date> field5() {
        return Information.INFORMATION.DATEAPPLIED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Information.INFORMATION.CONTACTNAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Information.INFORMATION.CONTACTMETHOD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Information.INFORMATION.CONTACTEDMEFIRST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return Information.INFORMATION.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return Information.INFORMATION.NOTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getCompany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date component5() {
        return getDateapplied();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getContactname();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getContactmethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getContactedmefirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getNotes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getCompany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date value5() {
        return getDateapplied();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getContactname();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getContactmethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getContactedmefirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getNotes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value2(String value) {
        setCompany(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value3(String value) {
        setPosition(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value4(String value) {
        setLocation(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value5(Date value) {
        setDateapplied(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value6(String value) {
        setContactname(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value7(String value) {
        setContactmethod(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value8(String value) {
        setContactedmefirst(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value9(String value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord value10(String value) {
        setNotes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InformationRecord values(Integer value1, String value2, String value3, String value4, Date value5, String value6, String value7, String value8, String value9, String value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InformationRecord
     */
    public InformationRecord() {
        super(Information.INFORMATION);
    }

    /**
     * Create a detached, initialised InformationRecord
     */
    public InformationRecord(Integer id, String company, String position, String location, Date dateapplied, String contactname, String contactmethod, String contactedmefirst, String status, String notes) {
        super(Information.INFORMATION);

        set(0, id);
        set(1, company);
        set(2, position);
        set(3, location);
        set(4, dateapplied);
        set(5, contactname);
        set(6, contactmethod);
        set(7, contactedmefirst);
        set(8, status);
        set(9, notes);
    }
}
