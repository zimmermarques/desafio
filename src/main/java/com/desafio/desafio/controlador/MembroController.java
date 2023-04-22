package com.desafio.desafio.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desafio.desafio.dominio.Pessoa;
import com.desafio.desafio.dominio.Projeto;
import com.desafio.desafio.dominio.repositorio.PessoaRepositorio;
import com.desafio.desafio.dominio.repositorio.ProjetoRepositorio;

@Controller
public class MembroController {
    
    @Autowired
    PessoaRepositorio pessoaRepositorio;

    @Autowired
	private ProjetoRepositorio projetoRepositorio; 

    @PostMapping(value = "/membro/salvar")
    public ResponseEntity<Object> salvar(@RequestBody Pessoa pessoa){
        Long proximoId = pessoaRepositorio.count()+1;

        pessoa.setId(Integer.valueOf(proximoId.toString()));
        
        if(pessoaRepositorio.findByNome(pessoa.getNome()).isEmpty()){
            pessoaRepositorio.save(pessoa);
        }else{
            return ResponseEntity.status(HttpStatus.valueOf(400)).body("Membro já existe com esse nome!");    
        }
        
        return ResponseEntity.ok().body(pessoa);
    }

    @RequestMapping("/adicionar-membro-projeto/{idMembro}/{idProjeto}")
	public Object addMembroProjeto(@PathVariable(name = "idMembro")  String idMembro, 
                                        @PathVariable(name = "idProjeto")  String idProjeto) {
        
        Optional<Projeto> projOp = projetoRepositorio.findById(Integer.valueOf(idProjeto));                  
        if(!projOp.isPresent()){
            return ResponseEntity.badRequest().body("Projeto não encontrado para a operação");
        }                                            
		Projeto projeto = projOp.get();

        Optional<Pessoa> memOp = pessoaRepositorio.findById(Integer.valueOf(idMembro));
        if(!memOp.isPresent()){
            return ResponseEntity.badRequest().body("Pessoa não encontrada para a operação");
        }                                            
        Pessoa membro = memOp.get();

        try {
            projeto.adicionarMembro(membro);
            projetoRepositorio.save(projeto);

            return "redirect:/";		
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	} 

    @RequestMapping("/remover-membro-projeto/{idMembro}/{idProjeto}")
	public Object removerMembroProjeto(@PathVariable(name = "idMembro") int idMembro, @PathVariable(name = "idProjeto") int idProjeto) {
        
        Optional<Projeto> projOp = projetoRepositorio.findById(Integer.valueOf(idProjeto));                  
        if(!projOp.isPresent()){
            return ResponseEntity.badRequest().body("Projeto não encontrado para a operação");
        }                                            
		Projeto projeto = projOp.get();

        Optional<Pessoa> memOp = pessoaRepositorio.findById(Integer.valueOf(idMembro));
        if(!memOp.isPresent()){
            return ResponseEntity.badRequest().body("Pessoa não encontrada para a operação");
        }                                            
        Pessoa membro = memOp.get();


        try {
            projeto.removerMembro(membro);
            projetoRepositorio.save(projeto);
            
            return "redirect:/";		
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	} 

}
