package ru.practicum.ewmserv.compilation.repository;

import ru.practicum.ewmserv.compilation.model.Compilation;

public interface CompilationRepositoryCustom {

    Compilation patchCompilationByAdmin(Compilation oldCompilation, Compilation newCompilation);
}