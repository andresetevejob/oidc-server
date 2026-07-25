package com.andre.core.services.admin;

import com.andre.core.model.ModelImpl;

public interface PersistenceService {
    void saveModel(ModelImpl model);
    ModelImpl loadModel();
}
