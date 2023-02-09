package ru.practicum.ewmserv.compilation.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.ewmserv.compilation.model.Compilation;
import ru.practicum.ewmserv.event.model.Event;

import java.util.ArrayList;

public class CompilationRepositoryImpl implements CompilationRepositoryCustom {
    private final CompilationRepository compilationRepository;

    public CompilationRepositoryImpl(@Lazy CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Compilation patchCompilationByAdmin(Compilation oldCompilation, Compilation newCompilation) {
        ArrayList<Event> eventList = (ArrayList<Event>) newCompilation.getEventList();
        boolean pinned = newCompilation.isPinned();
        String title = newCompilation.getTitle();

        if (!oldCompilation.getEventList().equals(eventList)) {
            oldCompilation.setEventList(eventList);
        }

        if (oldCompilation.isPinned() != pinned) {
            oldCompilation.setPinned(pinned);
        }

        if (!oldCompilation.getTitle().equals(title)) {
            oldCompilation.setTitle(title);
        }

        return compilationRepository.save(oldCompilation);
    }

}
