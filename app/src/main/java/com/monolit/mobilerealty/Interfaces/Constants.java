package com.monolit.mobilerealty.Interfaces;

public interface Constants {
    // 1C SERVER CONSTANTS
    String SERVER_NAMESPACE                   = "http://crm.monolit.net/RealyDataExchange";
    String SERVER_URL                         = "http://crm.monolit.net:90/crm_dev/ws/RealyDataExchange";
    String SERVER_SOAP_ACTION                 = "http://crm.monolit.net/RealyDataExchange#RealyDataExchange:";

    String SERVER_SOAP_METHOD_GET_USER_INFO = "getUserInfo";
    String SERVER_SOAP_METHOD_GET_OBJECTS = "getFreeObjects";
    String SERVER_SOAP_METHOD_GET_TASKS = "getTasks";
    String SERVER_SOAP_METHOD_GET_CLIENTS = "getClients";
    String SERVER_SOAP_METHOD_GET_RESERVATIONS = "getReservations";
    String SERVER_SOAP_METHOD_EXECUTE_TASK = "executeTask";
    String SERVER_SOAP_METHOD_OBJECT_RESERVATION = "objectReservation";
    String SERVER_SOAP_METHOD_GET_OBJECT_PLAN = "getObjectPlan";

    String PREFERENCE_LOGIN = "auth_login";
    String PREFERENCE_PASSWORD = "auth_password";
    String PREFERENCE_FULL_NAME = "fullName";
    String PREFERENCE_DEPARTMENT = "department";
    String PREFERENCE_PHONE_NUMBER = "phoneNumber";
    String PREFERENCE_SAVE_PASSWORD = "savePassword";
    String PREFERENCE_OLD_USER = "oldUser";
}
