package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/setores")
public class SetorController {

    @PostMapping
    public String addSetor(@RequestBody Setor setor) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(setor);
            FileWriter file = new FileWriter("setores.json", true);
            file.write(json + System.lineSeparator());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao salvar os dados";
        }
        return "Setor salvo com sucesso";
    }

    @GetMapping
    public List<Setor> getSetores() {
        List<Setor> setores = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<String> lines = Files.readAllLines(Paths.get("setores.json"));
            for (String line : lines) {
                Setor setor = mapper.readValue(line, Setor.class);
                setores.add(setor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return setores;
    }

    @DeleteMapping("/{id}")
    public String deleteSetor(@PathVariable Long id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("setores.json"));
            List<String> updatedLines = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            for (String line : lines) {
                Setor setor = mapper.readValue(line, Setor.class);
                if (!setor.getId().equals(id)) {
                    updatedLines.add(line);
                }
            }

            FileWriter file = new FileWriter("setores.json");
            for (String updatedLine : updatedLines) {
                file.write(updatedLine + System.lineSeparator());
            }
            file.close();

            return "Setor deletado com sucesso";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao deletar o setor";
        }
    }

    @PutMapping("/{id}")
    public String updateSetor(@PathVariable Long id, @RequestBody Setor setorAtualizado) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("setores.json"));
            List<String> updatedLines = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            boolean updated = false;

            for (String line : lines) {
                Setor setor = mapper.readValue(line, Setor.class);
                if (setor.getId().equals(id)) {
                    setor.setId(setorAtualizado.getId());
                    setor.setNome(setorAtualizado.getNome());
                    setor.setUnidadeId(setorAtualizado.getUnidadeId());
                    String updatedJson = mapper.writeValueAsString(setor);
                    updatedLines.add(updatedJson);
                    updated = true;
                } else {
                    updatedLines.add(line);
                }
            }

            if (!updated) {
                return "Setor não encontrado para atualização";
            }

            FileWriter file = new FileWriter("setores.json");
            for (String updatedLine : updatedLines) {
                file.write(updatedLine + System.lineSeparator());
            }
            file.close();

            return "Setor atualizado com sucesso";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao atualizar o setor";
        }
    }
}