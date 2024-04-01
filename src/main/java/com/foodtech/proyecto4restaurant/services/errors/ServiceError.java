package com.foodtech.proyecto4restaurant.services.errors;

/**
 * Error en tiempo de ejecución lanzado desde
 * las capas de servicio
 */
public class ServiceError extends RuntimeException{
    ErrorCode errorCode;

    public ServiceError(ErrorCode code, String message){
        super(message);
        this.errorCode = code;
    }
    /**
     * @return Identifica la causa del problema
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
