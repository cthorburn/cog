package com.cog.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.RuleNode;

import com.cog.lang.MuParser.Exit_loopContext;
import com.cog.lang.MuParser.Mult13ExprContext;

public class EvalVisitor extends MuBaseVisitor<Value> {


	static class Loop {
		public String name;
		public boolean _break;
		
		public Loop(String name) {
			
			this.name=name;
		}

		public void _break() {
			_break = true;
		}

		public boolean broken() {
			return _break;
		}
	}
	
	private Map<String, Value> memory = new HashMap<>();
	private Stack<Loop> loops=new Stack<>();

    @Override
    public Value visitIf_stat(MuParser.If_statContext ctx) {

        List<MuParser.Condition_blockContext> conditions =  ctx.condition_block();

        boolean evaluatedBlock = false;

        for(MuParser.Condition_blockContext condition : conditions) {

            Value evaluated = this.visit(condition.logicalAtom());

            if(evaluated.asBoolean()) {
                evaluatedBlock = true;
                // evaluate the block whose expr==true
                this.visit(condition.stat_block());
                break;
            }
        }

        if(!evaluatedBlock && ctx.stat_block() != null) {
            // evaluate the else-stat_block (if present == not null)
            this.visit(ctx.stat_block());
        }

        return Value.VOID;
    }

    @Override
    public Value visitAnd5Expr(MuParser.And5ExprContext ctx) {
        Value left = this.visit(ctx.logicalAtom());
        Value right = this.visit(ctx.and5Expr(0));
        return new Value(left.asBoolean() && right.asBoolean());
    }

    @Override
    public Value visitOr6Expr(MuParser.Or6ExprContext ctx) {
        Value left = this.visit(ctx.and5Expr(0));
        Value right = this.visit(ctx.and5Expr(1));
        return new Value(left.asBoolean() || right.asBoolean());
    }

    @Override
    public Value visitAdd12Expr(MuParser.Add12ExprContext ctx) {
        Value left = this.visit(ctx.numericAtom());
        Value right = this.visit(ctx.add12Expr(1));
        
        if(left.isNumber() && right.isNumber()) {
            return left.add(right);
        }
        else {
            return new Value(left.asString() + right.asString());
        }
    }

    @Override
    public Value visitAssignment(MuParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        Value value = this.visit(ctx.atom());
        return memory.put(id, value);
    }


    @Override
    public Value visitLogicalAtom(MuParser.LogicalAtomContext ctx) {
        return new Value(Boolean.valueOf(ctx.getText()));
    }

    @Override
    public Value visitLog(MuParser.LogContext ctx) {
        Value value = this.visit(ctx.expr());
        System.out.println(value);
        return value;
    }
	
    @Override 
	public Value visitWhile_stat(@NotNull MuParser.While_statContext ctx) {
    	loops.push(new Loop(""));
        Value value = this.visit(ctx.not14Expr());
        while(value.asBoolean()) {
        	visitChildren(ctx);
        	if(loops.peek().broken()) {
        		loops.pop();
        		break;
        	}
        	value = this.visit(ctx.not14Expr());
        }
        
        return Value.VOID;
    }
    
    
	

	@Override
	public Value visitMult13Expr(@NotNull Mult13ExprContext ctx) {
        Value left = this.visit(ctx.add12Expr(0));
        Value right = this.visit(ctx.add12Expr(1));
        
        return left.multiply(right);
	}

	@Override 
	public Value visitNumericAtom(@NotNull MuParser.NumericAtomContext ctx) {

		try {
	        return new Value(Integer.valueOf(ctx.getText()));
		}catch(Exception e) {
	        return new Value(Double.valueOf(ctx.getText()));
		}
		
	}

	@Override
	public Value visitExit_loop(Exit_loopContext ctx) {
		return Value.BREAK;
	}
	
	@Override
	protected boolean shouldVisitNextChild(@NotNull RuleNode node, Value currentResult) {
		boolean result = true;
		
		if(currentResult == Value.BREAK) {
			if(!loops.isEmpty()) {
				loops.peek()._break();
				result = false;
			}
			else {
	            throw new RuntimeException("break seen outside loop/switch construct");
			}
		}
		else if(!loops.isEmpty() && loops.peek().broken()) {
			result = false;
		}
		
		return result;
	}
	
	
}