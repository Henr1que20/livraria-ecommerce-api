package com.fatec.livrariaecommerce.controllers;

import com.fatec.livrariaecommerce.dao.CidadeDao;
import com.fatec.livrariaecommerce.dao.TipoEnderecoDao;
import com.fatec.livrariaecommerce.facade.ClientesFacade;
import com.fatec.livrariaecommerce.facade.EnderecoFacade;
import com.fatec.livrariaecommerce.facade.IFacade;
import com.fatec.livrariaecommerce.models.domain.*;
import com.fatec.livrariaecommerce.models.domain.Endereco;
import com.fatec.livrariaecommerce.models.dto.CidadeDTO;
import com.fatec.livrariaecommerce.models.dto.EnderecoDTO;
import com.fatec.livrariaecommerce.models.utils.Message;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/endereco")
public class EnderecoController {

    // ***********************************************************************
    private final IFacade facade;
    // ***********************************************************************

    @PostMapping(path = "{userId}")
    public ResponseEntity<Message> save(@PathVariable int userId, @RequestBody EnderecoDTO enderecoDto) {
        try {

            Usuario usuario = new Usuario();
            usuario.setId(userId);

            Cliente cliente = new Cliente();
            cliente.setUsuario(usuario);
            cliente = (Cliente) this.facade.consultar(cliente).getEntidades().get(0);

            Cidade cidade = new Cidade();
            cidade.setId(enderecoDto.getCidade().getId());
            cidade = (Cidade) this.facade.consultar(cidade).getEntidades().get(0);

            TipoEndereco tipoEndereco = new TipoEndereco();
            tipoEndereco.setId(enderecoDto.getTipoEndereco().getId());
            tipoEndereco = (TipoEndereco) this.facade.consultar(tipoEndereco).getEntidades().get(0);

            Endereco endereco = new Endereco(cliente);
            enderecoDto.fill(endereco, cidade, tipoEndereco);

            Resultado resultado = this.facade.salvar(endereco);

            Message message = new Message();

            if (resultado.getMensagem() == null) {
                message.setTitle("Sucesso");
                message.setDescription("Endereco cadastrado com sucesso!");
                return ResponseEntity.ok(message);
            } else {
                message.setTitle("Erro");
                message.setDescription(resultado.getMensagem());
                return ResponseEntity.badRequest().body(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ***********************************************************************

    @ResponseBody
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<EnderecoDTO>> listar(@PathVariable("idUsuario") int idUsuario) {
        try {
            Endereco endereco = new Endereco();
            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);

            Cliente cliente = new Cliente();
            cliente.setUsuario(usuario);
            cliente = (Cliente) this.facade.consultar(cliente).getEntidades().get(0);
            endereco.setCliente(cliente);

            List<EnderecoDTO> enderecos = this.facade.consultar(endereco).getEntidades().stream().map(ed -> {
                return EnderecoDTO.from((Endereco) ed);
            }).collect(Collectors.toList());

            return ResponseEntity.ok(enderecos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ***********************************************************************


}
