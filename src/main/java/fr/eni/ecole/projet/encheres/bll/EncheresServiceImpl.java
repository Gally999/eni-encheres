package fr.eni.ecole.projet.encheres.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.ecole.projet.encheres.bo.ArticleAVendre;
import fr.eni.ecole.projet.encheres.dal.ArticleAVendreDAO;

@Service
public class EncheresServiceImpl implements EncheresService {
	
	private ArticleAVendreDAO articleDAO;
	
	
	public EncheresServiceImpl(ArticleAVendreDAO articleDAO) {
		this.articleDAO = articleDAO;
	}

	@Override
	public List<ArticleAVendre> consulterEncheres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ArticleAVendre> consulterEncheresActives() {
		
		return articleDAO.findAllActive();
	}

}
