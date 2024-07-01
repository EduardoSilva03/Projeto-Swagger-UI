package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/unidades")
public class UnidadeController {

    @PostMapping
    public String addUnidade(@RequestBody Unidade unidade) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(unidade);
            FileWriter file = new FileWriter("unidades.json", true);
            file.write(json + System.lineSeparator());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao salvar os dados";
        }
        return "Unidade salva com sucesso";
    }

    @GetMapping
    public List<Unidade> getUnidades() {
        List<Unidade> unidades = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            List<String> lines = Files.readAllLines(Paths.get("unidades.json"));
            for (String line : lines) {
                Unidade unidade = mapper.readValue(line, Unidade.class);
                unidades.add(unidade);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unidades;
    }

    @DeleteMapping("/{id}")
    public String deleteUnidade(@PathVariable Long id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("unidades.json"));
            List<String> updatedLines = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            for (String line : lines) {
                Unidade unidade = mapper.readValue(line, Unidade.class);
                if (!unidade.getId().equals(id)) {
                    updatedLines.add(line);
                }
            }

            FileWriter file = new FileWriter("unidades.json");
            for (String updatedLine : updatedLines) {
                file.write(updatedLine + System.lineSeparator());
            }
            file.close();

            return "Unidade deletada com sucesso";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao deletar a unidade";
        }
    }

    @PutMapping("/{id}")
    public String updateUnidade(@PathVariable Long id, @RequestBody Unidade unidadeAtualizada) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("unidades.json"));
            List<String> updatedLines = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            boolean updated = false;

            for (String line : lines) {
                Unidade unidade = mapper.readValue(line, Unidade.class);
                if (unidade.getId().equals(id)) {
                    unidade.setId(unidadeAtualizada.getId());
                    unidade.setCnpj(unidadeAtualizada.getCnpj());
                    unidade.setRazao_social(unidadeAtualizada.getRazao_social());
                    unidade.setNome(unidadeAtualizada.getNome());
                    unidade.setEmail(unidadeAtualizada.getEmail());
                    unidade.setSite(unidadeAtualizada.getSite());
                    String updatedJson = mapper.writeValueAsString(unidade);
                    updatedLines.add(updatedJson);
                    updated = true;
                } else {
                    updatedLines.add(line);
                }
            }

            if (!updated) {
                return "Unidade não encontrada para atualização";
            }

            FileWriter file = new FileWriter("unidades.json");
            for (String updatedLine : updatedLines) {
                file.write(updatedLine + System.lineSeparator());
            }
            file.close();

            return "Unidade atualizada com sucesso";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao atualizar a unidade";
        }
    }
}