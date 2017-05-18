package com.oracle.fintech.smartgov.model.service;

import java.util.List;

import multichain.command.AddressCommand;
import multichain.command.ChainCommand;
import multichain.command.MultichainException;

public class TestCommand
{
  public TestCommand()
  {
    super();
  }

  public static void main(String[] args)
  {
    //BlockChain TestCommand has to be created and started before
    ChainCommand.initializeChain("TestCommand");

    List<String> result = null;
    try
    {
      result = AddressCommand.getAddresses();
    }
    catch (MultichainException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //result contains the addresses of the wallet as list of String.
  }
}
