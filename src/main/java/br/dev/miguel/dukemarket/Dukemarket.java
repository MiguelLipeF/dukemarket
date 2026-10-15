/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package br.dev.miguel.dukemarket;

import br.dev.miguel.dukemarket.bean.Produto;
import br.dev.miguel.dukemarket.dao.ProdutoDAO;

/**
 *
 * @author sesi2dib
 */
public class Dukemarket {

    public static void main(String[] args) {
        Produto pAux;
        ProdutoDAO pDAO = new ProdutoDAO();
        
/*
        //Inclusão de produto na DB
        pAux = new Produto();
        pAux.setCodBarras("11111111");
        pAux.setDescricao("Mouse Attack Shark");
        pAux.setQtd(50);
        pAux.setValorCompra(80.00);
        pAux.setValorVenda(167.00);

        pDAO.create(pAux);

        pAux = new Produto();
        pAux.setCodBarras("22222222");
        pAux.setDescricao("Teclado Gamer");
        pAux.setQtd(80);
        pAux.setValorCompra(104.00);
        pAux.setValorVenda(220.00);

        pDAO.create(pAux);

        pAux = new Produto();
        pAux.setCodBarras("33333333");
        pAux.setDescricao("Microcomputador Dell");
        pAux.setQtd(110);
        pAux.setValorCompra(2400.00);
        pAux.setValorVenda(3200.00);

        pDAO.create(pAux);
*/
/*
        // Deletar o produto na DB pelo ID
        pDAO.delete(1);
        pDAO.delete(2);
        pDAO.delete(4);
        pDAO.delete(5);
        pDAO.delete(6);
*/

        pAux = pDAO.getResultById(7);
        pAux.setQtd(100);
        pDAO.update(pAux);

        //Listar a DB
        pDAO.getResults().forEach(p -> {
            System.out.println(p.toString());
        });
    }
}
