package com.gtel.homework.apo;

public class UnauthorizedException extends IllegalArgumentException {
  public UnauthorizedException(String message) {
    super(message);
  }
}
