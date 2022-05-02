package com.company.controller;

import com.company.model.LibraryEvent;
import com.company.producer.LibraryEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/library-events")
@RequiredArgsConstructor
public class LibraryEventController {

    private final LibraryEventProducer libraryEventProducer;

    @PostMapping
    public HttpStatus createLibraryEvent(@RequestBody LibraryEvent libraryEvent) {
        libraryEventProducer.sendLibraryEventSync(libraryEvent);
        return HttpStatus.OK;
    }
}
