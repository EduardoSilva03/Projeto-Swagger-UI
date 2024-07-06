package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @PostMapping
    public String addFuncionario(@RequestBody Funcionario funcionario) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(funcionario);
            FileWriter file = new FileWriter("funcionarios.json", true);
            file.write(json + System.lineSeparator());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao salvar os dados";
        }
        return "Funcionario salvo com sucesso";
    }

    @GetMapping
    public List<Funcionario> getFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<String> lines = Files.readAllLines(Paths.get("funcionarios.json"));
            for (String line : lines) {
                Funcionario funcionario = mapper.readValue(line, Funcionario.class);
                funcionarios.add(funcionario);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return funcionarios;
    }

    @DeleteMapping("/{id}")
    public String deleteFuncionario(@PathVariable Long id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("funcionarios.json"));
            List<String> updatedLines = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            for (String line : lines) {
                Funcionario funcionario = mapper.readValue(line, Funcionario.class);
                if (!funcionario.getId().equals(id)) {
                    updatedLines.add(line);
                }
            }

            FileWriter file = new FileWriter("funcionarios.json");
            for (String updatedLine : updatedLines) {
                file.write(updatedLine + System.lineSeparator());
            }
            file.close();

            return "Funcionario deletado com sucesso";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao deletar o funcionario";
        }
    }

    @PutMapping("/{id}")
    public String updateFuncionario(@PathVariable Long id, @RequestBody Funcionario funcionarioAtualizado) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("funcionarios.json"));
            List<String> updatedLines = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            boolean updated = false;

            for (String line : lines) {
                Funcionario funcionario = mapper.readValue(line, Funcionario.class);
                if (funcionario.getId().equals(id)) {
                    funcionario.setId(funcionarioAtualizado.getId());
                    funcionario.setNome(funcionarioAtualizado.getNome());
                    funcionario.setSobrenome(funcionarioAtualizado.getSobrenome());
                    funcionario.setCpf(funcionarioAtualizado.getCpf());
                    funcionario.setRg(funcionarioAtualizado.getRg());
                    funcionario.setNome(funcionarioAtualizado.getNome());
                    funcionario.setUnidadeId(funcionarioAtualizado.getUnidadeId());
                    funcionario.setSetorId(funcionarioAtualizado.getSetorId());
                    String updatedJson = mapper.writeValueAsString(funcionario);
                    updatedLines.add(updatedJson);
                    updated = true;
                } else {
                    updatedLines.add(line);
                }
            }

            if (!updated) {
                return "Funcionario não encontrado para atualização";
            }

            FileWriter file = new FileWriter("funcionarios.json");
            for (String updatedLine : updatedLines) {
                file.write(updatedLine + System.lineSeparator());
            }
            file.close();

            return "Funcionario atualizado com sucesso";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao atualizar o funcionario";
        }
    }
}
