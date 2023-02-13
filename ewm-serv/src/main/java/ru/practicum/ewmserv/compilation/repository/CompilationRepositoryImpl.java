package ru.practicum.ewmserv.compilation.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.ewmserv.compilation.model.Compilation;
import ru.practicum.ewmserv.event.model.Event;

import java.util.List;
import java.util.Optional;

public class CompilationRepositoryImpl implements CompilationRepositoryCustom {
    private final CompilationRepository compilationRepository;

    public CompilationRepositoryImpl(@Lazy CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Compilation patchCompilationByAdmin(Compilation oldCompilation, Compilation newCompilation) {
        Optional<List<Event>> list = Optional.ofNullable(newCompilation.getEventList());
        boolean pinned = newCompilation.isPinned();
        String title = newCompilation.getTitle();

        if (list.isPresent()) {
            oldCompilation.setEventList(newCompilation.getEventList());
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
