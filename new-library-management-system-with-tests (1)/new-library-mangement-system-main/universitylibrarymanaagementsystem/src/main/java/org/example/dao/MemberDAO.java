package org.example.dao;

import org.example.Member;
import java.util.Collection;

public interface MemberDAO {

    void save(Member member);

    Member findById(String memberId);

    boolean deleteById(String memberId);

    Collection<Member> findAll();
}
