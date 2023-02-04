package com.example.applicationwithsocks.controllers;
import com.example.applicationwithsocks.model.Socks;
import com.example.applicationwithsocks.services.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/socks")
@Tag(name = "Носки",description = "CRUD-операции и другие эндпоинты для работы с носками")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping
    @Operation(
            summary = "Приёмка товара",
            description = "Вводим характеристику носков"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки приняты",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })
    public ResponseEntity<Long> addSocks(@RequestBody Socks socks) {
        long id = socksService.addSocks(socks);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{socksId}")
    @Operation(
            summary = "Поиск операций с товаром по id",
            description = "Вводим id "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Операции по id найдены",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })
    public ResponseEntity<Socks> getSocksById(@PathVariable Long socksId) {
        Socks socks = socksService.getSocksById(socksId);
        if (socks == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(socks);
    }

    @PutMapping("/{socksId}")
    @Operation(
            summary = "Поиск операций с носками по id для замены",
            description = "Вводим данные"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })
    public ResponseEntity editSocksById(@PathVariable Long socksId, @RequestBody Socks socks) {

        Socks socks1 = socksService.editSocksById(socksId, socks);
        if (socks1 == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(socks1);
    }

    @DeleteMapping
    @Operation(
            summary = "Списание всего товара",
            description = "Списываем носки со склада"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Носки списаны",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })
    public ResponseEntity<Void> deleteAllSocks() {
        socksService.deleteAllSocks();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{socksId}")
    @Operation(
            summary = "Удаление товара по номеру операции(id)",
            description = "Вводим id "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удалено!",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })
    public ResponseEntity<Void> deleteSocksbyId(@PathVariable Long socksId) {
        if (socksService.deleteSocks(socksId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(
            summary = "Получение данных обо всех товарах",
            description = "Все носки"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о товарах получены",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })
    public ResponseEntity<Collection<Socks>> getAllSocks() {
        Collection<Socks> allSocks = socksService.getAllSocks();
        if (allSocks == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allSocks);
    }

    @GetMapping("/recipeReport")
    @Operation(
            summary = "Получение отчёта о товаре в формате txt",
            description = "Все носки"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о товаре получены",
                    content= {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Socks.class))
                            )
                    }
            )
    })

    public ResponseEntity<InputStreamResource> getRecipesReport() {
        try {
            Path path = socksService.createReport();
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .contentLength(Files.size(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksLog.txt\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }
}