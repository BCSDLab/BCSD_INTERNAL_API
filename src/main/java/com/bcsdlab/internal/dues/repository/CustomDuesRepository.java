package com.bcsdlab.internal.dues.repository;

import java.util.List;

import com.bcsdlab.internal.dues.Dues;

public interface CustomDuesRepository {

    List<Dues> searchDues(Integer year, Long trackId);
}
