package com.github.wezzen.insight.exception.handle;

import com.github.wezzen.insight.dto.response.ErrorResponse;
import com.github.wezzen.insight.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFoundException(final CategoryNotFoundException e) {
        return new ErrorResponse("CATEGORY_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(DeleteNotEmptyCategoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDeleteNotEmptyCategoryException(final DeleteNotEmptyCategoryException e) {
        return new ErrorResponse("DELETE_NOT_EMPTY_CATEGORY", e.getMessage());
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateCategoryException(final DuplicateCategoryException e) {
        return new ErrorResponse("DUPLICATE_CATEGORY", e.getMessage());
    }

    @ExceptionHandler(DuplicateTagException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateTagException(final DuplicateTagException e) {
        return new ErrorResponse("DUPLICATE_TAG", e.getMessage());
    }

    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoteNotFoundException(final NoteNotFoundException e) {
        return new ErrorResponse("NOTE_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(DeletingTagWithNotesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDeletingTagWithNotesException(final DeletingTagWithNotesException e) {
        return new ErrorResponse("DELETE_NOT_EMPTY_TAG", e.getMessage());
    }

    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTagNotFoundException(final TagNotFoundException e) {
        return new ErrorResponse("TAG_NOT_FOUND", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(final IllegalArgumentException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(final Exception ex) {
        return new ErrorResponse("INTERNAL_ERROR", "Something went wrong.");
    }
}
