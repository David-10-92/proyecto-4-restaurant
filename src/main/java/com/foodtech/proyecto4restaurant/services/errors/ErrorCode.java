package com.foodtech.proyecto4restaurant.services.errors;

/**
 * Identifica las diferentes causas de un error
 * en la API
 */
public enum ErrorCode {
    /** Indica que los argumentos de entrada no son válidos */
    INVALID_INPUT(400),
    /** Indica que no se ha encontrado un recurso necesario */
    RESOURCE_NOT_FOUND(404),
    /** Un error genérico */
    SERVER_ERROR(500);

    private int httpErrorCode;
    private ErrorCode(int errCode){
        this.httpErrorCode = errCode;
    }
    public int getHttpErrorCode(){
        return httpErrorCode;
    }
}
