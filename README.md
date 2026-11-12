create database (seu_nome)_dukemarket;

use (seu_nome)_dukemarket;
create table produto (
 id INT auto_increment primary key,
 codBarras varchar(15),
 descricao varchar(30) NOT NULL,
 qtd decimal(10,2) default 0,
 valorCompra decimal(10,2) default 0,
 valorVenda decimal(10,2) default 0,
 dataCadastro datetime default current_timestamp);

CREATE TABLE cliente (
    id INT NOT NULL AUTO_INCREMENT,
    cpf BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    dataCadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    endereco VARCHAR(150),
    cidade VARCHAR(80),
    uf CHAR(2),
    PRIMARY KEY (id)
);
