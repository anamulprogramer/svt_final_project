package org.example.controller;

import org.example.Library;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("LibraryController tests using Mockito (mocked Library)")
public class LibraryControllerMockitoTest {

    @Mock
    private Library library;

    @Test
    @DisplayName("registerMember with valid id+name should call library.registerMember exactly once")
    void testRegisterMemberValidCallsLibrary() {
        LibraryController controller = new LibraryController(library);

        boolean result = controller.registerMember("STU001", "Rahman");

        assertTrue(result);
        verify(library, times(1)).registerMember(argThat(m ->
                m.getMemberId().equals("STU001") && m.getName().equals("Rahman")));
    }

    @Test
    @DisplayName("registerMember with blank id should be rejected WITHOUT ever calling Library")
    void testRegisterMemberBlankIdRejected() {
        LibraryController controller = new LibraryController(library);

        boolean result = controller.registerMember("", "Rahman");

        assertFalse(result);
        verifyNoInteractions(library);
    }

    @Test
    @DisplayName("registerMember with null name should be rejected WITHOUT ever calling Library")
    void testRegisterMemberNullNameRejected() {
        LibraryController controller = new LibraryController(library);

        boolean result = controller.registerMember("STU001", null);

        assertFalse(result);
        verifyNoInteractions(library);
    }

    @Test
    @DisplayName("addBook with all valid fields should call library.addBook exactly once")
    void testAddBookValidCallsLibrary() {
        LibraryController controller = new LibraryController(library);

        boolean result = controller.addBook("978-1", "Java Programming", "Anamul");

        assertTrue(result);
        verify(library, times(1)).addBook(argThat(b ->
                b.getIsbn().equals("978-1")
                        && b.getTitle().equals("Java Programming")
                        && b.getAuthor().equals("Anamul")));
    }

    @Test
    @DisplayName("addBook with a whitespace-only author should be rejected WITHOUT ever calling Library")
    void testAddBookBlankAuthorRejected() {
        LibraryController controller = new LibraryController(library);

        boolean result = controller.addBook("978-1", "Java Programming", "   ");

        assertFalse(result);
        verifyNoInteractions(library);
    }

    @Test
    @DisplayName("deleteMember with a valid id simply relays whatever boolean Library decides")
    void testDeleteMemberDelegatesResultFromLibrary() {
        when(library.deleteMember("STU001")).thenReturn(true);
        when(library.deleteMember("STU999")).thenReturn(false);

        LibraryController controller = new LibraryController(library);

        assertTrue(controller.deleteMember("STU001"));
        assertFalse(controller.deleteMember("STU999"));
    }

    @Test
    @DisplayName("deleteMember with a blank/whitespace id should be rejected WITHOUT ever calling Library")
    void testDeleteMemberBlankIdRejected() {
        LibraryController controller = new LibraryController(library);

        assertFalse(controller.deleteMember(" "));
        verifyNoInteractions(library);
    }

    @Test
    @DisplayName("issueBook simply forwards to library.issueBook and relays its boolean result")
    void testIssueBookDelegatesResultFromLibrary() {
        when(library.issueBook("978-1", "STU001")).thenReturn(true);

        LibraryController controller = new LibraryController(library);

        assertTrue(controller.issueBook("978-1", "STU001"));
        verify(library).issueBook("978-1", "STU001");
    }

    @Test
    @DisplayName("returnBook simply forwards to library.returnBook and relays its boolean result")
    void testReturnBookDelegatesResultFromLibrary() {
        when(library.returnBook("978-1", "STU001")).thenReturn(false);

        LibraryController controller = new LibraryController(library);

        assertFalse(controller.returnBook("978-1", "STU001"));
        verify(library).returnBook("978-1", "STU001");
    }

    @Test
    @DisplayName("getStatusReport simply returns whatever library.getStatusReport() produces")
    void testGetStatusReportDelegatesToLibrary() {
        when(library.getStatusReport()).thenReturn("FAKE REPORT");

        LibraryController controller = new LibraryController(library);

        assertEquals("FAKE REPORT", controller.getStatusReport());
    }
}
