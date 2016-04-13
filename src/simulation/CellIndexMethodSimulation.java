package simulation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import model.Cell;
import model.CellWrapper;
import model.Particle;
import model.SimulationData;

public class CellIndexMethodSimulation implements Simulation {
	private SimulationData simulationData;
	private Cell[][] cells;
	private int M;
	private boolean hasPeriodicBoundaries;
	
	public CellIndexMethodSimulation(Integer M, Boolean hasPeriodicBoundaries) {
		if (M == null || hasPeriodicBoundaries == null)
			throw new IllegalArgumentException("fuck everything");
		this.M = M;
		this.hasPeriodicBoundaries = hasPeriodicBoundaries;
	}
	
	@Override
	public void simulate(SimulationData simulationData) {
		this.simulationData = simulationData;
		initializeParticlesContainer();
		distributeParticles();
		calculateDistances();
	}
	
	private void calculateDistances() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				Cell cell = cells[i][j];
				List<Point> directions = generateDirections(i, j);
				List<CellWrapper> cellWrappers = calculateCellWrappers(directions);
				calculateDistances(cell, cellWrappers);
			}
		}
	}

	private void calculateDistances(Cell cell, List<CellWrapper> cellWrappers) {
		for (Particle particle: cell.getParticles()) {
			for (CellWrapper cellWrapper: cellWrappers) {
				Cell neighborCell = cellWrapper.getCell();
				for (Particle neighborParticle: neighborCell.getParticles()) {
					if (particle.equals(neighborParticle)) continue;
					if (!satisfiesDistance(particle, neighborParticle, cellWrapper)) continue;
					if (particle.getNeighbors().contains(neighborParticle)) continue;
					particle.getNeighbors().add(neighborParticle);
					neighborParticle.getNeighbors().add(particle);
				}
			}
		}
	}
	
	private boolean satisfiesDistance(Particle particleA, Particle particleB, CellWrapper particleBWrapper) {
		return distanceBetween(particleA, particleB, particleBWrapper) < simulationData.getInteractionRadius();
	}
	
	private double distanceBetween(Particle particleA, Particle particleB, CellWrapper particleBWrapper) {
		double ax = particleA.getX();
		double ay = particleA.getY();
		double bx = particleB.getX() + particleBWrapper.getxOffset();
		double by = particleB.getY() + particleBWrapper.getyOffset();
		return distanceBetween(ax, ay, bx, by) - particleA.getRadius() - particleB.getRadius();
	}
	
	private double distanceBetween(double x1, double y1, double x2, double y2) {
		double a = x1 - x2;
		double b = y1 - y2;
		return Math.sqrt(a * a + b * b);
	}

	private List<CellWrapper> calculateCellWrappers(List<Point> directions) {
		List<CellWrapper> cellWrappers = new ArrayList<CellWrapper>();
		for (Point direction : directions) {
			if (isOutOfBounds(direction.x, direction.y)
					&& !hasPeriodicBoundaries)
				continue;
			cellWrappers.add(generateCellWrapper(direction.x, direction.y));
		}
		return cellWrappers;
	}

	private List<Point> generateDirections(int i, int j) {
		List<Point> directions = new ArrayList<Point>();
		directions.add(new Point(i, j));
		directions.add(new Point(i - 1, j + 1));
		directions.add(new Point(i, j + 1));
		directions.add(new Point(i + 1, j + 1));
		directions.add(new Point(i + 1, j));
		return directions;
	}

	private boolean isOutOfBounds(int i, int j) {
		return !(i >= 0 && j >= 0 && i < M
				&& j < M);
	}

	private CellWrapper generateCellWrapper(int i, int j) {
		int xOffset = calculateOffset(j);
		int yOffset = calculateOffset(i);
		int realI = (i + M) % M;
		int realJ = (j + M) % M;
		Cell cell = cells[realI][realJ];
		return new CellWrapper(cell, xOffset, yOffset);
	}

	private int calculateOffset(int x) {
		if (x < 0)
			return -simulationData.getSpaceDimension();
		if (x >= M)
			return simulationData.getSpaceDimension();
		return 0;
	}

	private void distributeParticles() {
		for (Particle particle : simulationData.getParticles()) {
			int row = (int) Math.floor(particle.getY()
					/ getCellDimension());
			int column = (int) Math.floor(particle.getX()
					/ getCellDimension());
			cells[row][column].getParticles().add(particle);
		}
	}

	private void initializeParticlesContainer() {
		cells = new Cell[M][M];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				cells[i][j] = new Cell();
			}
		}
	}
	
	private Float getCellDimension() {
		if (simulationData.getSpaceDimension() == null)
			return null;
		return simulationData.getSpaceDimension() / ((float) M);
	}
}
