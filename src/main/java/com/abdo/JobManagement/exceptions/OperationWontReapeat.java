package com.abdo.JobManagement.exceptions;

public class OperationWontReapeat extends RuntimeException{

    public OperationWontReapeat(String mssg){
        super(mssg);
    }
}
