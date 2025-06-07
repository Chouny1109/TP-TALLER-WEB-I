package com.tallerwebi.repository;

import com.tallerwebi.model.RecoveryToken;

public interface RepositorioRecovery {

    Boolean guardar(RecoveryToken token);
    RecoveryToken buscarToken(String token);
    void actualizar(RecoveryToken token);
}
