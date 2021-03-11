package com.cattle.component.kettle.step;

public interface StepTypeInterface {

    enum FieldBelongType{

        SELECT_VALUE("selectValue"),CONSTANT("constant"),EXCEL_IMPORT("excelImport");


        private String name;

        FieldBelongType(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

}
