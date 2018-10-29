package com.tomgs.test.learn_enum;

/**
 * @author tangzhongyuan
 * @create 2018-09-10 17:43
 **/
public enum EventType {


    /**
     * Insert row.
     */
    INSERT("I"),

    /**
     * Update row.
     */
    UPDATE("U"),

    /**
     * Delete row.
     */
    DELETE("D"),

    /**
     * Create table.
     */
    CREATE("C"),

    /**
     * Alter table.
     */
    ALTER("A"),

    /**
     * Erase table.
     */
    ERASE("E"),

    /**
     * Query.
     */
    QUERY("Q"),

    /**
     * Truncate.
     */
    TRUNCATE("T"),

    /**
     * rename.
     */
    RENAME("R"),

    /**
     * create index.
     */
    CINDEX("CI"),

    /**
     * drop index.
     */
    DINDEX("DI");

    private String value;

    private EventType(String value){
        this.value = value;
    }

    public boolean isInsert() {
        return this.equals(EventType.INSERT);
    }

    public boolean isUpdate() {
        return this.equals(EventType.UPDATE);
    }

    public boolean isDelete() {
        return this.equals(EventType.DELETE);
    }

    public boolean isCreate() {
        return this.equals(EventType.CREATE);
    }

    public boolean isAlter() {
        return this.equals(EventType.ALTER);
    }

    public boolean isErase() {
        return this.equals(EventType.ERASE);
    }

    public boolean isQuery() {
        return this.equals(EventType.QUERY);
    }

    public boolean isTruncate() {
        return this.equals(EventType.TRUNCATE);
    }

    public boolean isRename() {
        return this.equals(EventType.RENAME);
    }

    public boolean isCindex() {
        return this.equals(EventType.CINDEX);
    }

    public boolean isDindex() {
        return this.equals(EventType.DINDEX);
    }

    public boolean isDdl() {
        return isCreate() || isAlter() || isErase() || isTruncate() || isRename() || isCindex() || isDindex();
    }

    public boolean isDml() {
        return isInsert() || isUpdate() || isDelete();
    }

    public static EventType valuesOf(String value) {
        EventType[] eventTypes = values();
        for (EventType eventType : eventTypes) {
            if (eventType.value.equalsIgnoreCase(value)) {
                return eventType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        System.out.println(EventType.ERASE.getValue());
    }

}
