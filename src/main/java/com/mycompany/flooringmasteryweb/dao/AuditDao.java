/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Audit;

import java.util.List;

/**
 *
 * @author ATeg
 */
public interface AuditDao {

    Audit create(Audit audit);
    Audit get(int id);
    int getSize();

    List<Audit> get();
    List<Audit> getResultRange(int start, int length);
    List<Audit> getWithPagination(int pageNumber, int resultsPerPage);   
}
