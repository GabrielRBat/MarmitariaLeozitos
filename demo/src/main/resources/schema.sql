CREATE TABLE IF NOT EXISTS marmita (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco DOUBLE NOT NULL,
    categoria VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    valor_total DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    cep_entrega VARCHAR(20),
    endereco_entrega VARCHAR(500),
    taxa_entrega DOUBLE NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    marmita_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DOUBLE NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (marmita_id) REFERENCES marmita(id)
);
