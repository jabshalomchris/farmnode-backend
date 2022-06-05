package com.project.farmnode.service;

import com.project.farmnode.dto.ProduceDto;
import com.project.farmnode.exception.ResourceNotFoundException;
import com.project.farmnode.mapper.ProduceMapper;
import com.project.farmnode.model.Produce;
import com.project.farmnode.model.User;
import com.project.farmnode.repository.ProduceRepo;
import com.project.farmnode.repository.SubscriptionRepo;
import com.project.farmnode.repository.UserRepo;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class ProduceServiceTest {

    @InjectMocks
    private ProduceService produceService;
    @Mock
    private ProduceRepo produceRepo;
    @Mock
    private ProduceMapper produceMapper;
    @Mock
    private UserRepo userRepo;
    @Mock
    private SubscriptionRepo subscriptionRepo;
    @Mock
    UserService userService;
    @Before
    public void setUp() {




    }

    @Test
    void testProduceSaving() {

        //given
        User user = new User(null,"Testing","testing@test4434.com","123","false","123", Instant.now(),true);

        //ProduceDto produce = new ProduceDto(1L,"Test","test","RIPE",2.00,"Vegetable","","each",2.2,2.2,"true","","","",0,false);
        Produce produce = new Produce(null,"Test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);

        //when
        when(userService.getUser(user.getUsername())).thenReturn(user);
        produceService.save(produce, user.getUsername());

        //then
        ArgumentCaptor<Produce> produceArgumentCaptor = ArgumentCaptor.forClass(Produce.class);
        verify(produceRepo).save(produceArgumentCaptor.capture());

        Produce capturedProduce = produceArgumentCaptor.getValue();
        assertThat(capturedProduce).isEqualTo(produce);
    }

    @Test
    void update() {
        //given
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);

        ProduceDto produceDto = new ProduceDto();
        produceDto.setProduceId(1L);


        //when
        when(produceMapper.mapUpdate(any(),any())).thenReturn(produce);

        produceService.update(produceDto,"test");

    }

    @Test
    void getAllProduce() {
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);

        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);


        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<ProduceDto> dtoResult = new ArrayList<>();
        dtoResult.add(dto1);

        //when
        when(produceRepo.findAll()).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);

        //then
        List<ProduceDto> resultList = produceService.getAllProduce();

        assertEquals(1, resultList.size()); // check size
        assertEquals(dtoResult, resultList); // check size
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce1); //verify the mapper
    }

    @Test
    void testGetProduceByID() {
        //given
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);
        ProduceDto produceDto = new ProduceDto();
        produceDto.setProduceId(1L);
        produceDto.setProduceName("tomato");
        produceDto.setProduceStatus("RIPE");
        produceDto.setPrice(1.00);
        produceDto.setMeasureType("each");

        //when
        when(produceRepo.findById(anyLong())).thenReturn(Optional.of(produce));
        when(produceMapper.mapToDto(any())).thenReturn(produceDto);

        ProduceDto result = produceService.getProduce(1L);
        //then
        assertEquals("tomato", result.getProduceName());
        assertNotNull(result);

    }

    @Test
    void testExceptionIsThrownWhenNotExistingUserIsUsedToGetProduce() {
        when(userRepo.findByUsername(any())).thenReturn(null);

        assertThatThrownBy(()->produceService.getProduceWithSubscription(1L,"testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testExceptionIsThrownWhenNotExistingProduceIsUsedToGetProduce() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        when(userRepo.findByUsername(any())).thenReturn(user);
        Long nonExistingId = 99L;
        //when
        //then
        assertThatThrownBy(()->produceService.getProduceWithSubscription(nonExistingId,"testingUname"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produce is not found with id: "+nonExistingId);
    }

    @Test
    void testGetProduceWithSubscription() {
        //given
        User user = new User(1L,"Testing","testingUname","123","file","123", Instant.now(),true);
        Long productId = 1L;
        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        ProduceDto expectedDto = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(produceRepo.findById(productId)).thenReturn(Optional.of(produce));
        when(subscriptionRepo.existsByUserAndProduce(any(),any())).thenReturn(true);
        //then
        ProduceDto result = produceService.getProduceWithSubscription(productId,user.getUsername());

        assertEquals(result, expectedDto);
    }

    @Test
    void testGetProduceWithSubscriptionWithInvalidUsername() {
        //given
        //when
        when(userRepo.findByUsername(any())).thenReturn(null);
        //then
        assertThatThrownBy(()->produceService.getProduceWithSubscription(1L,"testing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void testGetProduceByUsername() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);

        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);


        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<ProduceDto> dtoResult = new ArrayList<>();
        dtoResult.add(dto1);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(produceRepo.findByUser(any())).thenReturn(produceResult);
        //when(produceResult.stream().map(produceMapper::mapToDto).collect(Collectors.toList())).thenReturn(dtoResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);

        //then
        List<ProduceDto> resultList = produceService.getProduceByUsername(user.getUsername());

        assertEquals(1, resultList.size()); // check size
        assertEquals(dtoResult, resultList); // check size
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce1); //verify the mapper

    }
    @Test
    void testGetProduceByUsernameWithInvalidUsername() {
        //given
        String nonExistingUsername = "testing";
        //when
        when(userRepo.findByUsername(any())).thenReturn(null);
        //then
        assertThatThrownBy(()->produceService.getProduceByUsername(nonExistingUsername))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void getProduceByUserId() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);

        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<ProduceDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(dto1);

        //when
        when(userRepo.getById(any())).thenReturn(user);
        when(produceRepo.findByUser(any())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);

        //then
        List<ProduceDto> resultList = produceService.getProduceByUserId(user.getUserId());

        assertEquals(1, resultList.size()); // check size
        assertEquals(expectedDtoList,resultList); //check if its same with expected result
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce1); //verify the mapper
    }

    @Test
    void testExceptionInGetProduceByUserId() {
        //given
        //when
        when(userRepo.getById(any())).thenReturn(null);
        //then
        assertThatThrownBy(()->produceService.getProduceByUserId(1L))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void getProduceByUserIdForRequest() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);

        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<ProduceDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(dto1);

        //when
        when(produceRepo.findByUserForRequest(any())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);

        //then
        List<ProduceDto> resultList = produceService.getProduceByUserIdForRequest(user.getUserId());

        assertEquals(1, resultList.size()); // check size
        assertEquals(expectedDtoList,resultList); //check if its same with expected result
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce1); //verify the mapper

    }

    @Test
    void TestGetProduceByUsernameAndPublishStatus() {

        //given
        User user = new User();
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);

        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<ProduceDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(dto1);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(produceRepo.findByUserAndPublishStatus(any(),any())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);
        //then
        List<ProduceDto> resultList = produceService.getProduceByUsernameAndPublishStatus("test","true");

        assertEquals(1, resultList.size()); // check size
        assertEquals(expectedDtoList,resultList); //check if its same with expected result
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce1); //verify the mapper

    }

    @Test
    void TestExceptionInGetProduceByUsernameAndPublishStatus() {
        //given
        //when
        when(userRepo.findByUsername(any())).thenReturn(null);
        //then
        assertThatThrownBy(()->produceService.getProduceByUsernameAndPublishStatus("test","true"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found in the database");
    }

    @Test
    void TestGetFilteredProducesWithUsersProduce() {
        //given
        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);

        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        List<ProduceDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(dto1);

        //when
        when(produceRepo.findByFilters(any(),any(),any(),any(),any(),any())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);
        //then
        List<ProduceDto> resultList =
                produceService.getFilteredProduces("test","true","","","","","");

        assertEquals(1, resultList.size()); // check size
        assertEquals(expectedDtoList,resultList); //check if its same with expected result
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce1); //verify the mapper

    }

    @Test
    void TestGetFilteredProducesWithoutUsersProduce() {

        //given
        String username = "test";
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);

        Produce produce = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",user);
        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce);

        List<ProduceDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(dto1);

        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(produceRepo.findByFiltersOfNonusersProduce(any(),any(),any(),any(),any(),any(),anyLong())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);
        //then
        List<ProduceDto> resultList =
                produceService.getFilteredProduces("","","","","","",username);

        assertEquals(1, resultList.size()); // check size
        assertEquals(expectedDtoList,resultList); //check if its same with expected result
        assertEquals(dto1, resultList.get(0)); // check dto result
        Mockito.verify(produceMapper).mapToDto(produce); //verify the mapper
    }

    @Test
    void testGetFilteredGeoJsonProducesWithoutUsersProduce() {
        //given

        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);
        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);

        //when

        when(produceRepo.findByFilters(any(),any(),any(),any(),any(),any())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);

        //then
        String geoJsonProduces =
                produceService.getFilteredGeoJsonProduces("","","","","","","");

        assertNotNull(geoJsonProduces);
    }

    @Test
    void testGetFilteredGeoJsonProducesWithUsersProduce() {
        //given
        User user = new User(1L,"Testing","testingUname","123","false","123", Instant.now(),true);

        Produce produce1 = new Produce(1L,"test","check","RIPE",2.00,"Vegetable","each","address",2.2,2.2,"file","true",null);
        ProduceDto dto1 = new ProduceDto(1L,"test","check","RIPE",2.00,"Vegetable","address","each",2.2,2.2,"true","testingUname",null,"file",0,true);

        List<Produce> produceResult = new ArrayList<>();
        produceResult.add(produce1);



        //when
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(produceRepo.findByFiltersOfNonusersProduce(any(),any(),any(),any(),any(),any(),anyLong())).thenReturn(produceResult);
        when(produceMapper.mapToDto(any(Produce.class))).thenReturn(dto1);

        //then
        String geoJsonProduces =
                produceService.getFilteredGeoJsonProduces("","","","","","","user");

        assertNotNull(geoJsonProduces);
    }

    @Test
    void TestRipeStatusUpdateProduce() {
        produceService.updateProduceStatus("RIPE", 1);
    }
    @Test
    void TestGrowingStatusUpdateProduce() {
        
        produceService.updateProduceStatus("GROWING", 1);
    }

    @Test
    void updatePublishStatus() {
        //given
        //then
        produceService.updatePublishStatus("true", 1);

    }
}