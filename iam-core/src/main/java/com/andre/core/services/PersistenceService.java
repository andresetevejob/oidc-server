package com.andre.core.services;

import com.andre.core.model.ModelImpl;

public interface PersistenceService {
    void saveModel(ModelImpl model);
    ModelImpl getModel();
}
