package org.ticketplatform.java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ticketplatform.java.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

}