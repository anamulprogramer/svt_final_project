package org.example.dao.impl;

import org.example.Member;
import org.example.dao.MemberDAO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class InMemoryMemberDAO implements MemberDAO {

    private final Map<String, Member> members = new HashMap<>();

    @Override
    public void save(Member member) {
        members.put(member.getMemberId(), member);
    }

    @Override
    public Member findById(String memberId) {
        return members.get(memberId);
    }

    @Override
    public boolean deleteById(String memberId) {
        return members.remove(memberId) != null;
    }

    @Override
    public Collection<Member> findAll() {
        return members.values();
    }
}
