package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.dto.SneakerCartDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;

import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertPremiumSneaker;
import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertToSneakerCartDTOs;
import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertToSneakerDTO;
import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertToSneakerDTOList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SneakerControllerUnitTest {

    @InjectMocks
    private SneakerController sneakerController;

    @Mock
    private SneakerService sneakerService;

    private Sneaker sneaker;

    @BeforeEach
    void setup() {
        sneaker = Sneaker.builder()
                .id(1L)
                .name("Sneaker 1")
                .price(100.0)
                .gender(Gender.MALE)
                .description("Sneaker 1 Description")
                .brand(new Brand(1L, "Brand 1", new byte[]{}))
                .sneakerImages(new HashSet<>())
                .sneakerSizes(new HashSet<>())
                .build();
    }

    @Test
    void getPremiumSneaker_returnsValidPremiumSneakerResponseStructure() {
        // Arrange
        SneakerDTO actualSneakerDTO = convertPremiumSneaker(sneaker);
        when(sneakerService.findPremiumSneaker()).thenReturn(sneaker);

        // Act
        ResponseEntity<SneakerDTO> actualPremiumSneakerResponse = sneakerController.getPremiumSneaker();

        // Assert
        verify(sneakerService).findPremiumSneaker();
        assertThat(actualPremiumSneakerResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualPremiumSneakerResponse.getBody(), is(notNullValue()));
        assertThat(actualPremiumSneakerResponse.getBody(), is(equalTo(actualSneakerDTO)));
    }

    @Test
    void getMostPopularSneaker_returnsOkStatusWhenMostPopularSneakerFound() {
        // Arrange
        SneakerDTO actualSneakerDTO = convertPremiumSneaker(sneaker);
        when(sneakerService.findMostPopularSneaker()).thenReturn(sneaker);

        // Act
        final ResponseEntity<SneakerDTO> actualMostPopularSneakerResponse = sneakerController.getMostPopularSneaker();

        // Assert
        verify(sneakerService).findMostPopularSneaker();
        assertThat(actualMostPopularSneakerResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualMostPopularSneakerResponse.getBody(), is(equalTo(actualSneakerDTO)));
    }

    @Test
    void getSneaker_returnsOkAndCorrectSneakerDetails_whenSneakerExistsAndUserHasPermission() {
        // Arrange
        Long sneakerId = 1L;
        SneakerDTO expectedSneakerDTO = convertToSneakerDTO(sneaker);
        when(sneakerService.findById(sneakerId)).thenReturn(sneaker);

        // Act
        ResponseEntity<SneakerDTO> actualSneakerResponse = sneakerController.getSneaker(sneakerId);

        // Assert
        verify(sneakerService).findById(sneakerId);
        assertThat(actualSneakerResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualSneakerResponse.getBody(), is(equalTo(expectedSneakerDTO)));
    }

    @Test
    void getSneakerForCart_returnsSneakersInCorrectOrder_whenMultipleIdsProvided() {
        // Arrange
        List<Long> ids = List.of(1L);

        List<Sneaker> expectedSneakers = List.of(sneaker);
        when(sneakerService.findAllByIdWithImages(ids)).thenReturn(expectedSneakers);

        // Act
        ResponseEntity<List<SneakerCartDTO>> actualSneakersResponse = sneakerController.getSneakerForCart(ids);

        // Assert
        verify(sneakerService).findAllByIdWithImages(ids);
        assertThat(actualSneakersResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualSneakersResponse.getBody(), is(equalTo(convertToSneakerCartDTOs(expectedSneakers))));
    }

    @Test
    void getSneakers_returnsOkStatusAndCorrectSneakerList_whenPaginationParametersProvided() {
        // Arrange
        int page = 0;
        int size = 10;
        List<Sneaker> expectedSneakers = List.of(sneaker);
        when(sneakerService.findAllByPages(page, size, null, null, null, "name;asc", null)).thenReturn(expectedSneakers);

        // Act
        ResponseEntity<List<SneakerDTO>> actualSneakersResponse = sneakerController.getSneakers(page, size, null, null, null, "name;asc", null);

        // Assert
        verify(sneakerService).findAllByPages(page, size, null, null, null, "name;asc", null);
        assertThat(actualSneakersResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualSneakersResponse.getBody(), is(equalTo(convertToSneakerDTOList(expectedSneakers))));
    }

    @Test
    void getSneakers_returnsOkStatusAndCorrectSneakerList_whenUserHasPermission() {
        // Arrange
        List<Sneaker> expectedSneakers = List.of(sneaker);
        when(sneakerService.findAll()).thenReturn(expectedSneakers);

        // Act
        ResponseEntity<List<SneakerDTO>> actualSneakersResponse = sneakerController.getSneakers();

        // Assert
        verify(sneakerService).findAll();
        assertThat(actualSneakersResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualSneakersResponse.getBody(), is(equalTo(convertToSneakerDTOList(expectedSneakers))));
    }

    @Test
    void addSneaker_returnsSuccessfullyCreatedSneaker() {
        // Arrange
        SneakerDTO createSneakerDTO = SneakerDTO.builder()
                .name(sneaker.getName())
                .price(sneaker.getPrice())
                .gender(sneaker.getGender().ordinal())
                .brand(sneaker.getBrand().getName())
                .build();
        SneakerDTO actualSneakerDTO = convertToSneakerDTO(sneaker);
        when(sneakerService.create(createSneakerDTO)).thenReturn(sneaker);

        // Act
        ResponseEntity<SneakerDTO> actualCreatedSneakerResponse = sneakerController.addSneaker(createSneakerDTO);

        // Assert
        verify(sneakerService).create(createSneakerDTO);
        assertThat(actualCreatedSneakerResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualCreatedSneakerResponse.getBody(), is(equalTo(actualSneakerDTO)));
    }

    @Test
    void editSneaker_returnsSuccessfullyEditedSneaker() {
        // Arange
        SneakerDTO updateSneakerDTO = SneakerDTO.builder()
                .name("Test")
                .price(sneaker.getPrice())
                .gender(sneaker.getGender().ordinal())
                .brand(sneaker.getBrand().getName())
                .build();
        sneaker.setName("Test");
        SneakerDTO actualSneakerDTO = convertToSneakerDTO(sneaker);
        when(sneakerService.update(updateSneakerDTO)).thenReturn(sneaker);

        // Act
        ResponseEntity<SneakerDTO> actualUpdatedSneakerResponse = sneakerController.editSneaker(updateSneakerDTO);

        // Assert
        verify(sneakerService).update(updateSneakerDTO);
        assertThat(actualUpdatedSneakerResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualUpdatedSneakerResponse.getBody(), is(equalTo(actualSneakerDTO)));
    }

    @Test
    void deleteSneaker_returnsOkSuccessfullyDeletedSneaker() {
        // Arange
        // Act
        ResponseEntity<Void> actualDeleteSneakerResponse = sneakerController.deleteSneaker(1L);

        // Assert
        verify(sneakerService).deleteById(1L);
        assertThat(actualDeleteSneakerResponse.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void premiumSneaker_returnsOkSuccessfullyDeletedSneaker() {
        // Arange
        // Act
        ResponseEntity<Void> actualDeleteSneakerResponse = sneakerController.premiumSneaker(1L);

        // Assert
        verify(sneakerService).premium(1L);
        assertThat(actualDeleteSneakerResponse.getStatusCode(), is(HttpStatus.OK));
    }
}
