package com.desafio.desafio.controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desafio.desafio.dominio.ClassificacaoProjetoEnum;
import com.desafio.desafio.dominio.Pessoa;
import com.desafio.desafio.dominio.Projeto;
import com.desafio.desafio.dominio.ProjetoDTO;
import com.desafio.desafio.dominio.repositorio.PessoaRepositorio;
import com.desafio.desafio.dominio.repositorio.ProjetoRepositorio;

import net.minidev.json.JSONObject;


@Controller
public class AppController {

	@Autowired
	private ProjetoRepositorio projetoRepositorio; 

	@Autowired
	private PessoaRepositorio pessoaRepositorio;
	
	Projeto projeto = null;

	String errorProjetoNaoEncontrado = "Projeto não encontrado para a operação";

	String atributoProjeto = "projeto";

	@GetMapping("/")
	public ModelAndView irIndex() {
		ModelAndView mav = new ModelAndView("index");

		List<Projeto> projetoLista = new ArrayList<>();
		
		Iterator<Projeto> iterator = projetoRepositorio.findAll().iterator();
		while (iterator.hasNext()) {
			projetoLista.add(iterator.next());
		}

		mav.addObject("error", "");
		mav.addObject("projetoLista", projetoLista);
			
		return mav;
	}
	
 	@GetMapping("/novo")
	public ModelAndView novoProjeto(Model model) {
		ModelAndView mav = new ModelAndView("projeto-novo");
		
		projeto = new Projeto();

		List<ClassificacaoProjetoEnum> classLista = Arrays.asList(ClassificacaoProjetoEnum.values());
		model.addAttribute("classLista", classLista);
		model.addAttribute(atributoProjeto, projeto);

		mav.addObject("pessoaLista", getListaPessoa(false));

		return mav;
	}
	
	@PostMapping(value = "/salvar")
	public Object saveProjeto(@ModelAttribute("projeto") @Valid ProjetoDTO projeto) {
		
		Optional<Pessoa> memOp = pessoaRepositorio.findById(projeto.getGerenteId());
        if(!memOp.isPresent()){
            return ResponseEntity.badRequest().body("Pessoa não encontrada para a operação");
        }                                            
        Pessoa pessoa = memOp.get();
		
		Projeto projAux = new Projeto();
		
		BeanUtils.copyProperties(projeto, projAux);

		projAux.setGerente(pessoa);

		if(projAux.getId()==null){
			projAux.colocarEmAnalise();
			Long proximoId = projetoRepositorio.count()+1;
			projAux.setId(Integer.valueOf(proximoId.toString()));
		}

		projetoRepositorio.save(projAux);
		
		return "redirect:/";
	}

	@GetMapping("/editar/{id}")
	public ModelAndView editarProjeto(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("projeto-novo");
		try{

			Optional<Projeto> projOp = projetoRepositorio.findById(id);                  
        	if(!projOp.isPresent()){
				mav.setStatus(HttpStatus.BAD_REQUEST);
	            return mav;
        	} 
			projeto = projOp.get();

			mav.addObject(atributoProjeto, projeto);
			mav.addObject("statusProjeto", projeto.getStatus());

			List<Pessoa> membroLista = new ArrayList<>();
			Iterator<Pessoa> iterator = pessoaRepositorio.findAll().iterator();
			while (iterator.hasNext()) {
				Pessoa next = iterator.next();
				if(next.isFuncionario()){
					membroLista.add(next);
				}
			}

			membroLista.removeAll(projeto.getMembroLista());
			mav.addObject("membroLista", membroLista);
			mav.addObject("pessoaLista", getListaPessoa(false));

			return mav;
		}catch(Exception e){
			return irIndex();
		}
	}
	
	@GetMapping("/remover/{id}")
	public Object deleteProjeto(@PathVariable(name = "id") int id) {

		ModelAndView mav = null;

		Optional<Projeto> projOp = projetoRepositorio.findById(id);                  
		if(!projOp.isPresent()){
			return ResponseEntity.badRequest().body("Projeto não encontrado para a operação");
		}
		projeto = projOp.get();

		if(!projeto.isPodeExcluir()){
			mav = irIndex();

			mav.addObject("error", "Projeto não pode ser EXCLUÍDO!");
			return mav;
		}

		projetoRepositorio.deleteById(id);
		return "redirect:/";		
	} 

	@GetMapping("/selecionar-projeto/{id}")
	public @ResponseBody String selecionarProjeto(@PathVariable(name = "id") int id){

		Optional<Projeto> projOp = projetoRepositorio.findById(id);                  
		if(!projOp.isPresent()){
			return this.errorProjetoNaoEncontrado;
		}
		projeto = projOp.get();
		
		JSONObject obj = new JSONObject();
		obj.put("projeto", projeto);
		obj.put("statusProjeto", projeto.getStatus());
		obj.put("proximoStatus", projeto.getProximoStatusPossivel());

		return obj.toString();
	}

	@PostMapping(value = "/dar-continuidade/{id}")
	public Object darContinuidade(@PathVariable(name = "id") Integer id) {
		ModelAndView mav = irIndex();
		
		Optional<Projeto> projOp = projetoRepositorio.findById(id);                  
		if(!projOp.isPresent()){
			return ResponseEntity.badRequest().body(this.errorProjetoNaoEncontrado);
		}
		projeto = projOp.get();

		projeto.darContinuidade();

		projetoRepositorio.save(projeto);
		
		return mav;
	}

	private List<Pessoa> getListaPessoa(boolean isFuncionario){
		List<Pessoa> pessoaLista = new ArrayList<>();
		Iterator<Pessoa> iterator2 = pessoaRepositorio.findAll().iterator();
		while (iterator2.hasNext()) {
			Pessoa next = iterator2.next();
			if(next.isFuncionario()==isFuncionario){
				pessoaLista.add(next);
			}
		}
		return pessoaLista;
	}


}
