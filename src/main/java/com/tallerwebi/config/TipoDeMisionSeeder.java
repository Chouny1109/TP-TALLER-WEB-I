package com.tallerwebi.config;

import com.tallerwebi.dominio.enums.TIPO_MISION;
import com.tallerwebi.model.TipoDeMision;
import com.tallerwebi.repository.TipoDeMisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class TipoDeMisionSeeder {

    private final TipoDeMisionRepository tipoDeMisionRepository;

    @Autowired
    public TipoDeMisionSeeder(TipoDeMisionRepository tipoDeMisionRepository) {
        this.tipoDeMisionRepository = tipoDeMisionRepository;
    }

    @Transactional
    @PostConstruct
    public void init() {
        List<TIPO_MISION> tiposDeMision = new ArrayList<>();
        for (TIPO_MISION tipo : TIPO_MISION.values()) {
            if (!tipoDeMisionRepository.existePorNombre(tipo)) {
                tiposDeMision.add(tipo);
            }
        }
        tipoDeMisionRepository.saveAll(tiposDeMision);
    }
}
