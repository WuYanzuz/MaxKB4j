package com.asiainfo.model.service;


public interface STTModel {
    String speechToText(byte[] audioBytes, String suffix);
}
