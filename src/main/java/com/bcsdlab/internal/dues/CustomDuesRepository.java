package com.bcsdlab.internal.dues;

import java.util.List;

public interface CustomDuesRepository {

    List<Dues> searchDues(Integer year, Long trackId);
}
