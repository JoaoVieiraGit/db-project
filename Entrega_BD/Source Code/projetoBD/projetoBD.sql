-- phpMyAdmin SQL Dump
-- version 4.5.0.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 08-Dez-2015 às 17:55
-- Versão do servidor: 10.0.17-MariaDB
-- PHP Version: 5.6.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projetobd`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `alternativa`
--

CREATE TABLE `alternativa` (
  `IDprojeto` int(11) NOT NULL,
  `nomeAlternativa` varchar(11) NOT NULL,
  `IDalternativa` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `alternativa`
--

INSERT INTO `alternativa` (`IDprojeto`, `nomeAlternativa`, `IDalternativa`) VALUES
(3, 'Bicicleta', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `doacao`
--

CREATE TABLE `doacao` (
  `IDdoacao` int(11) NOT NULL,
  `montante` float NOT NULL,
  `IDuser` int(11) NOT NULL,
  `IDprojeto` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `doacao`
--

INSERT INTO `doacao` (`IDdoacao`, `montante`, `IDuser`, `IDprojeto`) VALUES
(1, 60, 1, 3),
(2, 60, 1, 3),
(3, 60, 1, 3),
(4, 60, 1, 3);

-- --------------------------------------------------------

--
-- Estrutura da tabela `mensagem`
--

CREATE TABLE `mensagem` (
  `IDmensagem` int(11) NOT NULL,
  `mensagem` varchar(4096) DEFAULT 'NOT NULL',
  `IDuser` int(11) NOT NULL,
  `IDprojeto` int(11) NOT NULL,
  `assunto` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `mensagem`
--

INSERT INTO `mensagem` (`IDmensagem`, `mensagem`, `IDuser`, `IDprojeto`, `assunto`) VALUES
(1, 'mensagem', 1, 3, 'random');

-- --------------------------------------------------------

--
-- Estrutura da tabela `projeto`
--

CREATE TABLE `projeto` (
  `IDprojeto` int(11) NOT NULL,
  `dataLimite` date NOT NULL,
  `descricao` text NOT NULL,
  `estado` varchar(200) NOT NULL,
  `nomeProjeto` varchar(50) NOT NULL,
  `valorObtido` float NOT NULL,
  `valorPedido` float NOT NULL,
  `IDuser` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `projeto`
--

INSERT INTO `projeto` (`IDprojeto`, `dataLimite`, `descricao`, `estado`, `nomeProjeto`, `valorObtido`, `valorPedido`, `IDuser`) VALUES
(3, '2015-10-13', 'projeto para a cadeira de BD', 'em curso', 'projetoBD', 0, 1000, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `recompensas`
--

CREATE TABLE `recompensas` (
  `IDrecompensas` int(11) NOT NULL,
  `valor` int(11) NOT NULL,
  `descricao` varchar(1024) NOT NULL,
  `IDprojeto` int(11) NOT NULL,
  `IDuser` int(11) NOT NULL,
  `IDdoacao` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `recompensas`
--

INSERT INTO `recompensas` (`IDrecompensas`, `valor`, `descricao`, `IDprojeto`, `IDuser`, `IDdoacao`) VALUES
(1, 60, 'carro', 3, 1, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `user`
--

CREATE TABLE `user` (
  `IDuser` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `saldo` int(11) NOT NULL DEFAULT '100'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `user`
--

INSERT INTO `user` (`IDuser`, `username`, `password`, `saldo`) VALUES
(1, 'conta', 'pass', 100);

-- --------------------------------------------------------

--
-- Estrutura da tabela `voto`
--

CREATE TABLE `voto` (
  `IDvoto` int(11) NOT NULL,
  `IDuser` int(11) NOT NULL,
  `valorVoto` int(11) NOT NULL,
  `IDalternativa` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `voto`
--

INSERT INTO `voto` (`IDvoto`, `IDuser`, `valorVoto`, `IDalternativa`) VALUES
(1, 1, 20, 1),
(2, 1, 60, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alternativa`
--
ALTER TABLE `alternativa`
  ADD PRIMARY KEY (`IDalternativa`),
  ADD KEY `IDprojeto` (`IDprojeto`);

--
-- Indexes for table `doacao`
--
ALTER TABLE `doacao`
  ADD PRIMARY KEY (`IDdoacao`),
  ADD KEY `IDuser` (`IDuser`),
  ADD KEY `IDprojeto` (`IDprojeto`);

--
-- Indexes for table `mensagem`
--
ALTER TABLE `mensagem`
  ADD PRIMARY KEY (`IDmensagem`),
  ADD KEY `IDprojeto` (`IDprojeto`),
  ADD KEY `IDuser` (`IDuser`);

--
-- Indexes for table `projeto`
--
ALTER TABLE `projeto`
  ADD PRIMARY KEY (`IDprojeto`),
  ADD KEY `IDuser` (`IDuser`);

--
-- Indexes for table `recompensas`
--
ALTER TABLE `recompensas`
  ADD PRIMARY KEY (`IDrecompensas`),
  ADD KEY `IDuser` (`IDuser`),
  ADD KEY `IDprojeto` (`IDprojeto`),
  ADD KEY `IDdoacao` (`IDdoacao`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`IDuser`);

--
-- Indexes for table `voto`
--
ALTER TABLE `voto`
  ADD PRIMARY KEY (`IDvoto`),
  ADD KEY `IDuser` (`IDuser`),
  ADD KEY `IDalternativa` (`IDalternativa`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `alternativa`
--
ALTER TABLE `alternativa`
  MODIFY `IDalternativa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `doacao`
--
ALTER TABLE `doacao`
  MODIFY `IDdoacao` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `mensagem`
--
ALTER TABLE `mensagem`
  MODIFY `IDmensagem` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `projeto`
--
ALTER TABLE `projeto`
  MODIFY `IDprojeto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `recompensas`
--
ALTER TABLE `recompensas`
  MODIFY `IDrecompensas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `IDuser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `voto`
--
ALTER TABLE `voto`
  MODIFY `IDvoto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `alternativa`
--
ALTER TABLE `alternativa`
  ADD CONSTRAINT `alternativa_ibfk_2` FOREIGN KEY (`IDprojeto`) REFERENCES `projeto` (`IDprojeto`);

--
-- Limitadores para a tabela `doacao`
--
ALTER TABLE `doacao`
  ADD CONSTRAINT `doacao_ibfk_1` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`),
  ADD CONSTRAINT `doacao_ibfk_2` FOREIGN KEY (`IDprojeto`) REFERENCES `projeto` (`IDprojeto`);

--
-- Limitadores para a tabela `mensagem`
--
ALTER TABLE `mensagem`
  ADD CONSTRAINT `mensagem_ibfk_1` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`),
  ADD CONSTRAINT `mensagem_ibfk_2` FOREIGN KEY (`IDprojeto`) REFERENCES `projeto` (`IDprojeto`);

--
-- Limitadores para a tabela `projeto`
--
ALTER TABLE `projeto`
  ADD CONSTRAINT `projeto_ibfk_1` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`);

--
-- Limitadores para a tabela `recompensas`
--
ALTER TABLE `recompensas`
  ADD CONSTRAINT `recompensas_ibfk_1` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`),
  ADD CONSTRAINT `recompensas_ibfk_2` FOREIGN KEY (`IDprojeto`) REFERENCES `projeto` (`IDprojeto`),
  ADD CONSTRAINT `recompensas_ibfk_3` FOREIGN KEY (`IDdoacao`) REFERENCES `doacao` (`IDdoacao`);

--
-- Limitadores para a tabela `voto`
--
ALTER TABLE `voto`
  ADD CONSTRAINT `voto_ibfk_1` FOREIGN KEY (`IDuser`) REFERENCES `user` (`IDuser`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
