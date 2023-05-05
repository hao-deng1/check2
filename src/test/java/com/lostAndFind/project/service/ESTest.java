package com.lostAndFind.project.service;

import com.lostAndFind.project.es.service.EsLostService;
import com.lostAndFind.project.es.service.EsPickService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

/**
 * @author yc_
 * @version 1.0
 */
public class ESTest {

    @Autowired
    private EsPickService esPickService;

    @Autowired
    private EsLostService esLostService;

}
