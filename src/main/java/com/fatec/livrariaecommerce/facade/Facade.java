package com.fatec.livrariaecommerce.facade;

import com.fatec.livrariaecommerce.dao.*;
import com.fatec.livrariaecommerce.models.domain.*;
import com.fatec.livrariaecommerce.strategy.IStrategy;
import com.fatec.livrariaecommerce.strategy.cliente.criptografia.CriptografarSenha;
import com.fatec.livrariaecommerce.strategy.cliente.criptografia.DescriptografarSenha;
import com.fatec.livrariaecommerce.strategy.cliente.validaemail.ValidaEmail;
import com.fatec.livrariaecommerce.strategy.cupom.TipoCupom;
import com.fatec.livrariaecommerce.strategy.cupom.ValidaCupomUsados;
import com.fatec.livrariaecommerce.strategy.itenspedido.GerarCupom;
import com.fatec.livrariaecommerce.strategy.itenspedido.SolicitarTroca;
import com.fatec.livrariaecommerce.strategy.livro.ValidaEntradaEstoque;
import com.fatec.livrariaecommerce.strategy.venda.*;
import com.fatec.livrariaecommerce.strategy.cartao.SalvarCartaoFuturaCompra;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Facade implements IFacade {

    // ***********************************************************************

    // key => Nome da classe, value => Instancia do dao
    private Map<String, IDAO> daos = new HashMap<>();

    // key => Nome da classe, value => Map( key => Operacao, value => Instancia da regra de negocio )
    Map<String, Map<String, List<IStrategy>>> regrasNegocio = new HashMap<>();

    // ***********************************************************************

    public Facade(ClienteDao clienteDao, UsuarioDao usuarioDao, EnderecoDao enderecoDao, CidadeDao cidadeDao,
                  TipoEnderecoDao tipoEnderecoDao, TelefoneDao telefoneDao, CartaoCreditoDao cartaoCreditoDao,
                  LivroDao livroDao, GrupoPrecificacaoDao grupoPrecificacaoDao, CategoriaDao categoriaDao, VendaDao vendaDao, CupomDao cupomDao, ItensPedidoDao itensPedidoDao, NotificacaoDao notificacaoDao) {

        // ClienteDao
        this.daos.put(Cliente.class.getName(), clienteDao);

        // UsuarioDao
        this.daos.put(Usuario.class.getName(), usuarioDao);

        // EnderecoDao
        this.daos.put(Endereco.class.getName(), enderecoDao);

        // CidadeDao
        this.daos.put(Cidade.class.getName(), cidadeDao);

        // TipoEnderecoDao
        this.daos.put(TipoEndereco.class.getName(), tipoEnderecoDao);

        // TelefoneDao
        this.daos.put(Telefone.class.getName(), telefoneDao);

        //CartaoCreditoDao
        this.daos.put(CartaoCredito.class.getName(), cartaoCreditoDao);

        //LivroDao
        this.daos.put(Livro.class.getName(), livroDao);

        //GrupoPrecificacaoDao
        this.daos.put(GrupoPrecificacao.class.getName(), grupoPrecificacaoDao);

        //CategoriaDao
        this.daos.put(Categoria.class.getName(), categoriaDao);

        //VendaDao
        this.daos.put(Venda.class.getName(), vendaDao);

        //CupomDao
        this.daos.put(Cupom.class.getName(), cupomDao);

        //itensPedidoDao
        this.daos.put(ItensPedido.class.getName(), itensPedidoDao);

        //notificacaoDao
        this.daos.put(Notificacao.class.getName(), notificacaoDao);

    }

    // ***********************************************************************

    @PostConstruct
    public void postConstruct() {

        // ***********************************************************************
        // Cliente
        Map<String, List<IStrategy>> regrasNegocioCliente = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarCliente = new ArrayList<>();

        rnsSalvarCliente.add(new CriptografarSenha());
        rnsSalvarCliente.add(new ValidaEmail(this));

        regrasNegocioCliente.put("SALVAR", rnsSalvarCliente);

        List<IStrategy> rnsAlterarCliente = new ArrayList<>();
        regrasNegocioCliente.put("ALTERAR", rnsAlterarCliente);

        List<IStrategy> rnsExcluirCliente = new ArrayList<>();
        regrasNegocioCliente.put("EXCLUIR", rnsExcluirCliente);

        List<IStrategy> rnsConsultarCliente = new ArrayList<>();
        regrasNegocioCliente.put("CONSULTAR", rnsConsultarCliente);
        this.regrasNegocio.put(Cliente.class.getName(), regrasNegocioCliente);


        // ***********************************************************************
        // Endereco
        Map<String, List<IStrategy>> regrasNegocioEndereco = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarEndereco = new ArrayList<>();
        regrasNegocioEndereco.put("SALVAR", rnsSalvarEndereco);

        List<IStrategy> rnsAlterarEndereco = new ArrayList<>();
        regrasNegocioEndereco.put("ALTERAR", rnsAlterarEndereco);

        List<IStrategy> rnsExcluirEndereco = new ArrayList<>();
        regrasNegocioEndereco.put("EXCLUIR", rnsExcluirEndereco);

        List<IStrategy> rnsConsultarEndereco = new ArrayList<>();
        regrasNegocioEndereco.put("CONSULTAR", rnsConsultarEndereco);

        this.regrasNegocio.put(Endereco.class.getName(), regrasNegocioEndereco);


        // ***********************************************************************
        // Telefone
        Map<String, List<IStrategy>> regrasNegocioTelefone = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarTelefone = new ArrayList<>();
        regrasNegocioTelefone.put("SALVAR", rnsSalvarTelefone);

        List<IStrategy> rnsAlterarTelefone = new ArrayList<>();
        regrasNegocioTelefone.put("ALTERAR", rnsAlterarTelefone);

        List<IStrategy> rnsExcluirTelefone = new ArrayList<>();
        regrasNegocioTelefone.put("EXCLUIR", rnsExcluirTelefone);

        List<IStrategy> rnsConsultarTelefone = new ArrayList<>();
        regrasNegocioTelefone.put("CONSULTAR", rnsConsultarTelefone);

        this.regrasNegocio.put(Telefone.class.getName(), regrasNegocioTelefone);


        // ***********************************************************************
        // Usuario
        Map<String, List<IStrategy>> regrasNegocioUsuario = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsConsultarUsuario = new ArrayList<>();
        rnsConsultarUsuario.add(new DescriptografarSenha());
        regrasNegocioUsuario.put("CONSULTAR", rnsConsultarUsuario);

        this.regrasNegocio.put(Usuario.class.getName(), regrasNegocioUsuario);


        // ***********************************************************************
        // Cidade
        Map<String, List<IStrategy>> regrasNegocioCidade = new HashMap<>();

        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsConsultarCidade = new ArrayList<>();
        regrasNegocioCidade.put("CONSULTAR", rnsConsultarCidade);

        this.regrasNegocio.put(Cidade.class.getName(), regrasNegocioCidade);


        // ***********************************************************************
        // Tipo Endereco
        Map<String, List<IStrategy>> regrasNegocioTipoEndereco = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsConsultarTipoEndereco = new ArrayList<>();
        regrasNegocioTipoEndereco.put("CONSULTAR", rnsConsultarTipoEndereco);

        this.regrasNegocio.put(TipoEndereco.class.getName(), regrasNegocioTipoEndereco);


        // ***********************************************************************
        // Cartao Credito
        Map<String, List<IStrategy>> regrasNegocioCartaoCredito = new HashMap<>();

        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarCartaoCredito = new ArrayList<>();
        rnsSalvarCartaoCredito.add(new SalvarCartaoFuturaCompra());
        regrasNegocioCartaoCredito.put("SALVAR", rnsSalvarCartaoCredito);

        List<IStrategy> rnsAlterarCartaoCredito = new ArrayList<>();
        regrasNegocioCartaoCredito.put("ALTERAR", rnsAlterarCartaoCredito);

        List<IStrategy> rnsExcluirCartaoCredito = new ArrayList<>();
        regrasNegocioCartaoCredito.put("EXCLUIR", rnsExcluirCartaoCredito);

        List<IStrategy> rnsConsultarCartaoCredito = new ArrayList<>();
        regrasNegocioCartaoCredito.put("CONSULTAR", rnsConsultarCartaoCredito);

        this.regrasNegocio.put(CartaoCredito.class.getName(), regrasNegocioCartaoCredito);

        // ***********************************************************************
        // Livro
        Map<String, List<IStrategy>> regrasNegocioLivro = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarLivro = new ArrayList<>();
        rnsSalvarLivro.add(new ValidaEntradaEstoque());
        regrasNegocioLivro.put("SALVAR", rnsSalvarLivro);

        List<IStrategy> rnsAlterarLivro = new ArrayList<>();
        regrasNegocioLivro.put("ALTERAR", rnsAlterarLivro);

        List<IStrategy> rnsExcluirLivro = new ArrayList<>();
        regrasNegocioLivro.put("EXCLUIR", rnsExcluirLivro);

        List<IStrategy> rnsConsultarLivro = new ArrayList<>();
        regrasNegocioLivro.put("CONSULTAR", rnsConsultarLivro);

        this.regrasNegocio.put(Livro.class.getName(), regrasNegocioLivro);

        // ***********************************************************************
        // GrupoPrecificacao
        Map<String, List<IStrategy>> regrasNegocioGrupoPrecificacao = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarGrupoPrecificacao = new ArrayList<>();
        regrasNegocioGrupoPrecificacao.put("SALVAR", rnsSalvarGrupoPrecificacao);

        List<IStrategy> rnsAlterarGrupoPrecificacao = new ArrayList<>();
        regrasNegocioGrupoPrecificacao.put("ALTERAR", rnsAlterarGrupoPrecificacao);

        List<IStrategy> rnsExcluirGrupoPrecificacao = new ArrayList<>();
        regrasNegocioGrupoPrecificacao.put("EXCLUIR", rnsExcluirGrupoPrecificacao);

        List<IStrategy> rnsConsultarGrupoPrecificacao = new ArrayList<>();
        regrasNegocioGrupoPrecificacao.put("CONSULTAR", rnsConsultarGrupoPrecificacao);

        this.regrasNegocio.put(GrupoPrecificacao.class.getName(), regrasNegocioGrupoPrecificacao);


        // ***********************************************************************
        // Categoria
        Map<String, List<IStrategy>> regrasNegocioCategoria = new HashMap<>();
        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarCategoria = new ArrayList<>();
        regrasNegocioCategoria.put("SALVAR", rnsSalvarCategoria);

        List<IStrategy> rnsAlterarCategoria = new ArrayList<>();
        regrasNegocioCategoria.put("ALTERAR", rnsAlterarCategoria);

        List<IStrategy> rnsExcluirCategoria = new ArrayList<>();
        regrasNegocioCategoria.put("EXCLUIR", rnsExcluirCategoria);

        List<IStrategy> rnsConsultarCategoria = new ArrayList<>();
        regrasNegocioCategoria.put("CONSULTAR", rnsConsultarCategoria);

        this.regrasNegocio.put(Categoria.class.getName(), regrasNegocioCategoria);

        // ***********************************************************************
        // Venda
        Map<String, List<IStrategy>> regrasNegocioVenda = new HashMap<>();

        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarVenda = new ArrayList<>();
        rnsSalvarVenda.add(new ValidaCupom((VendaDao) this.daos.get(Venda.class.getName())));
        rnsSalvarVenda.add(new ValidaCartaoCredito((CartaoCreditoDao) this.daos.get(CartaoCredito.class.getName())));
        rnsSalvarVenda.add(new GerenciarEstoqueVenda((LivroDao) this.daos.get(Livro.class.getName())));
        rnsSalvarVenda.add(new ValidaValorPagoCartoes());
        regrasNegocioVenda.put("SALVAR", rnsSalvarVenda);

        List<IStrategy> rnsAlterarVenda = new ArrayList<>();
        rnsAlterarVenda.add(new AlteraStatusVenda((VendaDao) this.daos.get(Venda.class.getName())));
        rnsAlterarVenda.add(new CancelarVenda());
        regrasNegocioVenda.put("ALTERAR", rnsAlterarVenda);

        List<IStrategy> rnsExcluirVenda = new ArrayList<>();
        regrasNegocioVenda.put("EXCLUIR", rnsExcluirVenda);

        List<IStrategy> rnsConsultarVenda = new ArrayList<>();
        regrasNegocioVenda.put("CONSULTAR", rnsConsultarVenda);

        this.regrasNegocio.put(Venda.class.getName(), regrasNegocioVenda);


        // ***********************************************************************
        // Cupom
        Map<String, List<IStrategy>> regrasNegocioCupom = new HashMap<>();

        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarCupom = new ArrayList<>();
        rnsSalvarCupom.add(new TipoCupom((CupomDao) this.daos.get(Cupom.class.getName())));
        regrasNegocioCupom.put("SALVAR", rnsSalvarCupom);

        List<IStrategy> rnsAlterarCupom = new ArrayList<>();
        regrasNegocioCupom.put("ALTERAR", rnsAlterarCupom);

        List<IStrategy> rnsExcluirCupom = new ArrayList<>();
        regrasNegocioCupom.put("EXCLUIR", rnsExcluirCupom);

        List<IStrategy> rnsConsultarCupom = new ArrayList<>();
        rnsConsultarCupom.add(new ValidaCupomUsados((VendaDao) this.daos.get(Venda.class.getName())));
        regrasNegocioCupom.put("CONSULTAR", rnsConsultarCupom);

        this.regrasNegocio.put(Cupom.class.getName(), regrasNegocioCupom);


        // ***********************************************************************
        // ItensPedido
        Map<String, List<IStrategy>> regrasNegocioItensPedido = new HashMap<>();

        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarItensPedido = new ArrayList<>();
        regrasNegocioItensPedido.put("SALVAR", rnsSalvarItensPedido);

        List<IStrategy> rnsAlterarItensPedido = new ArrayList<>();
        rnsAlterarItensPedido.add(new SolicitarTroca((NotificacaoDao) this.daos.get(Notificacao.class.getName())));
        rnsAlterarItensPedido.add(new GerarCupom((LivroDao) this.daos.get(Livro.class.getName())));
        regrasNegocioItensPedido.put("ALTERAR", rnsAlterarItensPedido);

        List<IStrategy> rnsExcluirItensPedido = new ArrayList<>();
        regrasNegocioItensPedido.put("EXCLUIR", rnsExcluirItensPedido);

        List<IStrategy> rnsConsultarItensPedido = new ArrayList<>();
        regrasNegocioItensPedido.put("CONSULTAR", rnsConsultarItensPedido);

        this.regrasNegocio.put(ItensPedido.class.getName(), regrasNegocioItensPedido);

        // ***********************************************************************
        // ItensPedido
        Map<String, List<IStrategy>> regrasNegocioNotificacao = new HashMap<>();

        // Instanciar classes de regras de negocio e adicionar na lista de rns
        List<IStrategy> rnsSalvarNotificacao = new ArrayList<>();
        regrasNegocioNotificacao.put("SALVAR", rnsSalvarNotificacao);

        List<IStrategy> rnsAlterarNotificacao = new ArrayList<>();
        regrasNegocioNotificacao.put("ALTERAR", rnsAlterarNotificacao);

        List<IStrategy> rnsExcluirNotificacao = new ArrayList<>();
        regrasNegocioNotificacao.put("EXCLUIR", rnsExcluirNotificacao);

        List<IStrategy> rnsConsultarNotificacao = new ArrayList<>();
        regrasNegocioNotificacao.put("CONSULTAR", rnsConsultarNotificacao);

        this.regrasNegocio.put(Notificacao.class.getName(), regrasNegocioNotificacao);

    }

    // ***********************************************************************

    @Override
    public Resultado salvar(EntidadeDominio dominio) {
        Resultado resultado = new Resultado();
        List<IStrategy> rns = this.regrasNegocio.get(dominio.getClass().getName()).get("SALVAR");

        StringBuilder sb = this.executarRegras(dominio, rns);

        if (sb.length() == 0) {
            dominio = this.daos.get(dominio.getClass().getName()).salvar(dominio);
            resultado.getEntidades().add(dominio);
        } else {
            resultado.getEntidades().add(dominio);
            resultado.setMensagem(sb.toString());
        }
        return resultado;
    }

    @Override
    public Resultado alterar(EntidadeDominio dominio) {
        Resultado resultado = new Resultado();
        List<IStrategy> rns = this.regrasNegocio.get(dominio.getClass().getName()).get("ALTERAR");

        StringBuilder sb = this.executarRegras(dominio, rns);

        if (sb.length() == 0) {
            this.daos.get(dominio.getClass().getName()).salvar(dominio);
            resultado.getEntidades().add(dominio);
        } else {
            resultado.getEntidades().add(dominio);
            resultado.setMensagem(sb.toString());
        }
        return resultado;
    }

    @Override
    public Resultado excluir(EntidadeDominio dominio) {
        Resultado resultado = new Resultado();
        this.daos.get(dominio.getClass().getName()).excluir(dominio);
        return resultado;
    }

    @Override
    public Resultado consultar(EntidadeDominio dominio) {
        Resultado resultado = new Resultado();
        List<IStrategy> rns = this.regrasNegocio.get(dominio.getClass().getName()).get("CONSULTAR");
        if (rns != null && rns.size() > 0) {
            StringBuilder sb = this.executarRegras(dominio, rns);
        }
        resultado.getEntidades().addAll(this.daos.get(dominio.getClass().getName()).consultar(dominio));
        return resultado;
    }

    //    visualizar é semelhante ao getOne();
    @Override
    public Resultado visualizar(EntidadeDominio dominio) {
        return null;
    }

    // ***********************************************************************

    public StringBuilder executarRegras(EntidadeDominio dominio, List<IStrategy> rnsEntidade) {
        StringBuilder sb = new StringBuilder();

        for (IStrategy rn : rnsEntidade) {
            String msg = rn.processar(dominio);
            if (msg != null) {
                sb.append(msg);
            }
        }
        return sb;
    }

    // ***********************************************************************


}
